# Adding JWT Authentication to a CRUD API

This guide describes a practical way to add Spring Security and JWT authentication to a CRUD application. It uses the Splitwise `UserService` as the example, while explaining how the same pieces fit together in a new application.

The goal is to keep responsibilities clear:

- Spring Security authenticates login credentials.
- The application issues a JWT after successful login.
- A request filter validates JWTs on later requests and establishes the caller's identity.
- Authorization rules decide which endpoints require that identity.
- Controllers and CRUD services continue to handle application operations.

> The current Splitwise code has pieces of this design, but its `SecurityConfig` currently uses `.anyRequest().permitAll()`. Therefore, most API routes are not presently required to have a valid JWT. This guide describes the intended protected CRUD setup and points out the difference where relevant.

## 1. Start with the application's data model and CRUD API

Build the ordinary application structure first. For Splitwise UserService, the main user record is `User`, with persistence handled by `UserRepository`/`UserDao`, services, and controllers.

Keep the domain model focused on application data. Spring Security has its own user contract, so it is usually better to adapt the domain model rather than make the entity itself implement Spring Security interfaces.

A common controller organization is:

- `AuthController`: registration, login, and other account-authentication routes.
- `UserController` or other resource controllers: normal CRUD routes.

This split is a useful convention, not a Spring Security requirement. Security applies to requests independently of which controller class contains a handler.

## 2. Decide which endpoints are public

List the routes that must work before login. For the current UserService, these include:

- `POST /api/user/register`
- `POST /api/user/login`

Password reset or recovery routes need a deliberate design. A route that changes a password should not be public merely because it is related to authentication; it needs a secure recovery mechanism or an already-authenticated user's authorization.

Swagger/OpenAPI routes may also need public access during development. Add explicit rules for the documentation endpoints if needed.

Everything else should normally require authentication, for example the user's CRUD routes.

## 3. Create a Spring Security configuration

Create a configuration class such as `SecurityConfig` (the class name is arbitrary) and annotate it with `@Configuration`. This tells Spring to process the bean definitions in the class; the annotation itself does not intercept requests.

Use `@Bean` methods for objects that Spring Security needs to manage, such as:

- A `PasswordEncoder`, commonly BCrypt.
- A `DaoAuthenticationProvider` configured with the application's `UserDetailsService` and `PasswordEncoder`.
- An `AuthenticationManager` obtained from Spring Security's `AuthenticationConfiguration`.
- A `SecurityFilterChain` containing route authorization rules and the JWT filter.

Spring creates and manages these objects during application startup. They can then be injected into other Spring-managed classes. The `SecurityFilterChain` is the piece that connects security rules and filters to incoming HTTP requests.

### What each object is responsible for

- **`PasswordEncoder`:** hashes passwords when accounts are created or passwords are changed, and compares a submitted password with the stored hash during login. Do not store raw passwords.
- **`UserDetailsService`:** retrieves a user account using the login identifier, which is email in Splitwise.
- **`DaoAuthenticationProvider`:** uses the `UserDetailsService` to load account details and the `PasswordEncoder` to check the submitted password.
- **`AuthenticationManager`:** coordinates the authentication attempt and delegates to a suitable provider.
- **`SecurityFilterChain`:** configures which requests are public or protected and which filters run for those requests.

The provider's bean method is a place to construct and configure a provider, not to write per-request password validation logic.

## 4. Adapt the application's user to Spring Security

Spring Security expects a `UserDetails` object. In Splitwise, `CustomUserDetail` implements `UserDetails` and wraps the application's `User` object.

The adapter exposes information Spring needs, such as:

- Username: Splitwise uses the email as the username.
- Password: the stored encoded password.
- Authorities: roles or permissions, if the application has them.
- Account flags: whether the account is enabled, locked, or expired.

`CustomUserDetailsService` implements `UserDetailsService`. Its lookup method finds the `User` by email using the repository and returns a `CustomUserDetail` around it. If no account exists, it reports that the username was not found.

This keeps the persistence model separate from Spring Security's interface while allowing Spring to authenticate against the application's database.

## 5. Make registration store encoded passwords

Registration should:

1. Receive and validate registration details.
2. Check that the email is not already registered.
3. Build the application user record.
4. Encode the submitted password with the `PasswordEncoder`.
5. Save the user and return a response that does not expose the password or password hash.

In the current Splitwise UserService, `AuthController.registerNewUser()` delegates to `AuthService.registerUser()`, which delegates user creation to `UserService`/`UserServicesImpl`. `UserServicesImpl.createUser()` encodes the password before saving it.

Be careful when reusing a general `createUser` method for updates: encoding an already-encoded password again can break future login. Password encoding should happen when setting a new raw password, not blindly every time an existing entity is saved.

## 6. Route login through Spring Security

The login endpoint should not perform its own database lookup and password comparison if the application is using Spring Security's provider flow.

Conceptually, the login path is:

```text
POST /api/user/login
  -> AuthController receives email and password
  -> AuthService passes credentials to AuthenticationManager
  -> AuthenticationManager delegates to DaoAuthenticationProvider
  -> provider calls CustomUserDetailsService
  -> service loads User and returns CustomUserDetail
  -> provider uses PasswordEncoder to compare passwords
  -> successful Authentication returns to AuthService
  -> AuthService creates a JWT for the authenticated identity
  -> controller returns the token
```

If the credentials are invalid or the user does not exist, authentication fails and token creation must not happen. The login service's main jobs are to request authentication and, after success, issue the application's token; it should not duplicate the provider's lookup and password-checking work.

`AuthenticationManager` is created as a Spring bean during startup, but it does not automatically receive each login request. The login service passes the submitted credentials to it when the login endpoint is called.

## 7. Create and validate JWTs

A JWT helper such as Splitwise's `JwtUtil` can own token operations:

- Create a signed token after successful login.
- Store an identity claim, such as the email or a stable user ID, as the subject.
- Set issue and expiration times.
- Parse and validate the signature and expiration on later requests.

The signing key must be stable across application restarts and shared consistently by every service that needs to validate the token. Splitwise currently creates a random key when `JwtUtil` initializes. That means a restart changes the key and invalidates tokens issued before that restart. For a persistent application, load a sufficiently strong secret from secure configuration rather than generating a new key on each process start.

A JWT is signed, not encrypted by default. Do not put passwords or other secrets in its claims.

## 8. Add the JWT request filter

A filter such as Splitwise's `JwtAuthenticationFilter` extends `OncePerRequestFilter`. Register it in the `SecurityFilterChain` before Spring Security's `UsernamePasswordAuthenticationFilter` position.

For a later request, the filter should conceptually:

1. Read the `Authorization` header.
2. Look for the `Bearer` token format.
3. Validate the token signature and expiration.
4. Read the identity claim from the validated token.
5. Load the corresponding user details if the application needs current account or authority data.
6. Create an authenticated Spring Security `Authentication` and place it in `SecurityContextHolder`.
7. Continue the filter chain.

If no token is present, the filter normally leaves the context unauthenticated and continues the chain. The filter's job is to establish identity from a token; the authorization rules decide whether the request may continue without identity.

Handle invalid and malformed tokens deliberately. A malformed token should not accidentally become an unhandled server error. Decide how authentication failures are returned, typically as an unauthorized response, and ensure invalid credentials do not result in token issuance.

## 9. Set route authorization rules

The filter chain should explicitly permit the public routes first, then require authentication for the remaining routes. In Splitwise, that means permitting login and registration, and then protecting other API paths with an authenticated rule.

The current configuration instead says `.anyRequest().permitAll()`. As a result:

- The JWT filter still runs.
- A valid token may populate `SecurityContextHolder`.
- Requests without a token are still authorized to reach other controller routes.

Changing the catch-all rule to require authentication makes the populated security context important. For a protected route, a valid JWT must result in an authenticated `Authentication` in the context before authorization runs. Otherwise Spring Security denies the request before Spring MVC calls the controller.

If a route has finer requirements, such as only the owner of a record may update it, authentication alone is not enough. Add authorization checks based on the authenticated identity and the requested resource.

## 10. Understand the full request flow

For a protected CRUD API such as `GET /api/user/{id}`:

```text
HTTP request
  -> Spring Security selects the configured SecurityFilterChain
  -> JWT filter checks the Authorization header
  -> valid token establishes Authentication in SecurityContextHolder
  -> authorization rule requires an authenticated request
  -> Spring MVC finds a matching controller method
  -> controller calls application service
  -> service/repository performs the CRUD operation
  -> response is returned
```

Without a token, the JWT filter continues without setting authentication. The authorization rule then denies the request. If no controller route matches, the request can still return 404 after it passes security; authentication does not create a controller mapping.

## 11. Test the behavior manually

Use a simple sequence to verify the behavior:

1. Register a user and confirm the database stores an encoded password, not the original password.
2. Log in with the correct password and confirm the response includes a token.
3. Try an incorrect password and confirm no token is returned.
4. Call a protected CRUD endpoint without a token and confirm it is denied.
5. Call it with a valid bearer token and confirm it reaches the controller.
6. Try an expired, altered, and malformed token and confirm each is rejected cleanly.
7. Restart the service and confirm tokens behave as intended with the configured signing key.
8. If roles or ownership rules exist, verify a user cannot access or modify another user's protected data.

## 12. Splitwise UserService class map

| Responsibility | Splitwise example |
|---|---|
| HTTP login/register endpoints | `AuthController` |
| Login, registration, and password-reset orchestration | `AuthService` |
| Security beans and endpoint rules | `SecurityConfig` |
| Spring Security user adapter | `CustomUserDetail` |
| Load user record for Spring Security | `CustomUserDetailsService` |
| Hash and compare passwords | `PasswordEncoder` bean and `UserServicesImpl` |
| Create/parse/validate JWTs | `JwtUtil` |
| Read bearer token and establish request identity | `JwtAuthenticationFilter` |
| Domain CRUD endpoints | `UserController` and other resource controllers |

## Final mental model

- `@Configuration` tells Spring where to find bean definitions; it is not the request interceptor.
- `@Bean` creates shared Spring-managed objects during startup.
- The `SecurityFilterChain` is run for requests by Spring Security's filter infrastructure.
- The login service explicitly sends credentials to `AuthenticationManager`.
- The provider, user-details service, and password encoder perform the credential validation path.
- The login service issues a JWT only after authentication succeeds.
- The JWT filter establishes an authenticated request identity from a valid token.
- Authorization rules decide whether that identity is mandatory for an API.
- Controllers remain responsible for application endpoints; they do not call the security configuration directly.
