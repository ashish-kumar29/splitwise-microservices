
# 🔐 Spring Boot JWT Authentication (Login + Authorization)

This project demonstrates **Login API** with **JWT Authentication** in Spring Boot.  
After logging in, users receive a token that must be sent with every protected request.  

---

## 🚀 Flow of Authentication

### 1️⃣ User sends login request
- **Endpoint:** `POST /login`
- **Request body:**
```json
{
  "email": "user@example.com",
  "password": "123456"
}
````

---

### 2️⃣ Controller handles request

* `AuthController.login()` receives the request.
* Delegates to `AuthService.login()`.

---

### 3️⃣ AuthService authenticates

1. Looks up user from DB via `UserRepository.findByEmail(email)`.

    * If not found → throw `"Invalid email"`.
2. Verifies password with `BCryptPasswordEncoder.matches(raw, hashed)`.

    * If mismatch → throw `"Invalid password"`.
3. If valid → calls `JwtUtil.generateToken(email)`.

---

### 4️⃣ JWT is generated

* `JwtUtil` builds a token with:

    * **subject** = user’s email
    * **issuedAt** = now
    * **expiration** = 1 hour later
    * **signed with** secret key (HS256)

---

### 5️⃣ Response sent to client

* `AuthService` wraps token in `LoginResponse`.
* `AuthController` returns:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.abcd1234..."
}
```

---

### 6️⃣ Client stores token

* Typically stored in:

    * **localStorage** (Frontend apps)
    * **Session storage**
    * **Cookies**

---

### 7️⃣ Client makes secured requests

* Every request to a protected endpoint must include:

```
Authorization: Bearer <token>
```

---

### 8️⃣ JwtAuthenticationFilter intercepts request

1. Extracts JWT from **Authorization** header.
2. Calls `jwtUtil.extractEmail(token)`.
3. Loads user from DB via `CustomUserDetailsService`.
4. Validates token (subject + expiry).
5. If valid → sets authentication in `SecurityContextHolder`.

---

### 9️⃣ Request proceeds

* Spring Security now sees the request as **authenticated**.
* The controller method executes normally.

---

## 🧪 Testing with Postman

### 1. Login to get token

* Method: **POST**
* URL: `http://localhost:8080/login`
* Body → `raw` → `JSON`:

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

* Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.abcd1234..."
}
```

---

### 2. Use token for protected APIs

* Method: **GET**
* URL: `http://localhost:8080/api/employees`
* Go to **Headers** tab in Postman:

    * **Key**: `Authorization`
    * **Value**: `Bearer eyJhbGciOiJIUzI1NiJ9.abcd1234...`

✅ Now your request will pass authentication.

---

## 🔑 Key Points

* `JwtUtil` → creates & validates tokens.
* `JwtAuthenticationFilter` → checks token in every request.
* `SecurityContextHolder` → stores authentication once validated.
* Without valid token → Spring Security blocks access with `401 Unauthorized`.
