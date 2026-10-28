# API Authentication Flow

This document describes how authentication is wired in the UserService and what happens to requests at runtime.

## 1. Spring setup at application startup

Spring Boot starts the application context and scans the application package (and its subpackages) for Spring components and configuration classes.

### `@Configuration`

`@Configuration` marks `SecurityConfig` as a source of Spring bean definitions. It does not, by itself, intercept HTTP requests. Spring processes the bean methods in this class and registers their returned objects in the application context.

### `@Bean`

`@Bean` on a method tells Spring to call that method during application-context setup and manage the returned object as a bean. Other Spring-managed objects can receive beans through constructor or other dependency injection.

In `SecurityConfig`, the bean methods provide:

- `PasswordEncoder`: a `BCryptPasswordEncoder` used by the application to encode and compare passwords.
- `DaoAuthenticationProvider`: configured with `CustomUserDetailsService` and the password encoder. It supports Spring Security's provider-based username/password authentication flow.
- `AuthenticationManager`: obtained from Spring Security's `AuthenticationConfiguration`.
- `SecurityFilterChain`: the HTTP security rules and filters used to process requests.

The application context also discovers `JwtAuthenticationFilter` and `CustomUserDetailsService` because they are Spring components (`@Component` and `@Service`). The constructor of `SecurityConfig` receives those managed objects.

## 2. The filter chain and request processing

`SecurityFilterChain` is the important connection between the configuration and incoming HTTP requests. Spring Security's servlet filter infrastructure applies the configured chain to matching requests before they reach Spring MVC controllers.

The chain in `SecurityConfig`:

1. Disables CSRF protection.
2. Permits `/api/user/login` and `/api/user/register` without authentication.
3. Applies the configured authorization rule to other requests.
4. Adds `JwtAuthenticationFilter` before Spring Security's `UsernamePasswordAuthenticationFilter` position.

The order of the configuration method calls in `SecurityConfig` is not the same thing as the runtime order of filters. `addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)` specifies the JWT filter's position in the filter chain. Spring Security runs the chain in its filter order for each request.

### What `JwtAuthenticationFilter` does

`JwtAuthenticationFilter` extends `OncePerRequestFilter`; Spring's servlet filter infrastructure invokes its `doFilterInternal` method as part of the chain. For each request, it:

1. Reads the `Authorization` header.
2. If the value starts with `Bearer `, extracts the token and asks `JwtUtil` for its email subject.
3. If an email was extracted and the `SecurityContext` has no authentication yet, loads the user using `CustomUserDetailsService`.
4. Checks the token with `JwtUtil.isTokenValid(...)`.
5. If valid, creates a `UsernamePasswordAuthenticationToken` with the `CustomUserDetail` as its principal and stores it in `SecurityContextHolder`.
6. Calls `filterChain.doFilter(request, response)` so processing continues to the next filter.

If there is no Bearer token, the filter does not set authentication; it still calls `filterChain.doFilter(...)`. This filter does not itself require a token or deny a request. The authorization rule in the filter chain determines whether an unauthenticated request may proceed.

## 3. What happens to an API request

For a request such as `GET /api/groups` (assuming a controller is mapped to it):

```text
HTTP request
  -> Spring Security filter chain
  -> JwtAuthenticationFilter
       -> no Bearer token: leave SecurityContext unauthenticated
       -> valid Bearer token: load user and set Authentication in SecurityContext
  -> authorization rule
  -> Spring MVC finds a matching controller method
  -> controller/service/repository process the request
```

With `.anyRequest().permitAll()`, the authorization step allows the request whether or not the filter set an authenticated user. A valid token can still make the caller's identity available in the `SecurityContext`, but authentication is not mandatory for those endpoints.

If the rule is changed to `.anyRequest().authenticated()`, Spring Security requires an authenticated `Authentication` in the `SecurityContext` for every request not matched by an earlier public rule. A valid JWT processed by the filter supplies that authentication. Without it, Spring Security denies access before the controller is called (normally with an unauthorized response, depending on the configured entry point).

A controller is not called when no controller mapping matches the HTTP method and path. Security processing happens before Spring MVC's controller mapping, so a request can pass authorization and still receive `404 Not Found` if there is no matching handler.

## 4. Registration flow in this project

For `POST /api/user/register`:

```text
Request
  -> security filter chain
  -> JWT filter (token is optional for this route)
  -> route is permitted
  -> AuthController.registerNewUser(...)
  -> AuthService.registerUser(...)
  -> check whether email already exists
  -> build User
  -> UserService.createUser(...)
  -> UserServicesImpl hashes password with PasswordEncoder and saves user
  -> return registration response
```

The password is encoded in `UserServicesImpl.createUser()` before persistence. Registration is explicitly permitted in `SecurityConfig`.

## 5. Login flow in this project

For `POST /api/user/login`:

```text
Request
  -> security filter chain
  -> JWT filter (an existing token is not required)
  -> route is permitted
  -> AuthController.login(...)
  -> AuthService.login(...)
  -> find user by email
  -> compare request password with stored password using PasswordEncoder.matches(...)
  -> JwtUtil.generateToken(email)
  -> return token in LoginResponse
```

The current `AuthService.login()` performs the user lookup and password check itself. It does not call the configured `AuthenticationManager`. Therefore the `AuthenticationManager` and `DaoAuthenticationProvider` beans are not used by this current login path. The JWT filter also does not use them; it validates a JWT and builds an authentication object for the request.

## 6. Purpose of `CustomUserDetail`

Spring Security defines the `UserDetails` interface as its standard view of an application user. The project's `CustomUserDetail` adapts the application's `User` model to that interface:

- `getUsername()` returns the email.
- `getPassword()` returns the stored password value (in this project, it should be the encoded value).
- `getAuthorities()` currently returns an empty collection, so no roles or permissions are provided.
- The account-status methods currently all return `true`.

`CustomUserDetailsService.loadUserByUsername(email)` finds the user in `UserRepository` and returns `new CustomUserDetail(user)`. In the current JWT request flow, `JwtAuthenticationFilter` uses this service to load the principal after extracting the email from a token.

## 7. Important current behavior and caveats

- The active `SecurityConfig` uses `.anyRequest().permitAll()`. This means other APIs are not currently protected by authentication, even though the JWT filter can identify requests that provide a valid token.
- To require a valid authenticated context for other APIs, use `.anyRequest().authenticated()` after the public route matchers. If Swagger UI or OpenAPI endpoints should stay public, add explicit permit rules for their paths before the catch-all authenticated rule.
- `AuthService.login()` has a fallback comparison against the raw stored password after `PasswordEncoder.matches(...)`. Registration uses BCrypt encoding, so the raw-password fallback is not needed for normally registered users and should not be treated as a safe password-storage approach.
- `JwtUtil` creates its signing key in a static initializer with `Keys.secretKeyFor(...)`. This generates a new key when the process starts; tokens from before an application restart will not validate after the key changes. A stable secret should be supplied through secure configuration for tokens intended to survive restarts.
- The filter parses the token before checking `isTokenValid`. Malformed or otherwise unparsable tokens can raise a parsing exception; the filter does not currently catch that exception and turn it into a deliberate authentication response.
- `JwtAuthenticationFilter` loads the user before checking `isTokenValid`. This ordering could be adjusted so token validation happens before the database lookup.

## 8. Summary

- `@Configuration` makes `SecurityConfig` a source of bean definitions; it is not itself a per-request interceptor.
- `@Bean` creates Spring-managed objects during application startup.
- `SecurityFilterChain` connects the configured rules and filters to HTTP request processing.
- Spring Security invokes `JwtAuthenticationFilter` for requests through the servlet filter chain.
- The JWT filter can put an authenticated user in `SecurityContextHolder`; the authorization rule decides whether that authentication is required.
- The current catch-all rule is `permitAll()`, so the APIs are currently accessible without a JWT.
