
---

# User Service – Spring Boot

A simple **User Service** built with Spring Boot.
Supports **user registration, login, and JWT-based authentication**.
This project can be extended into a microservice architecture.

---

## 📌 Features

* User Registration API
* User Login API with JWT generation
* JWT Validation for secured endpoints
* Layered Architecture (Controller → Service → DAO → Repository → DB)
* Spring Security integration

---

## 🏗️ High-Level Architecture

```mermaid
flowchart TD
    A[Client] --> B[Controller]
    B --> C[Service]
    C --> D[DAO / Repository]
    D --> E[(Database)]
```

---

## 🔑 Login Flow (JWT Generation)

```mermaid
sequenceDiagram
    participant User
    participant AuthController
    participant AuthService
    participant UserRepository
    participant JwtUtil

    User->>AuthController: POST /login (email, password)
    AuthController->>AuthService: login(request)
    AuthService->>UserRepository: findByEmail(email)
    UserRepository-->>AuthService: User
    AuthService->>AuthService: validate password
    AuthService->>JwtUtil: generateToken(email)
    JwtUtil-->>AuthService: JWT
    AuthService-->>AuthController: LoginResponse(JWT)
    AuthController-->>User: 200 OK + JWT
```

---

## 🔒 Secured API Flow (JWT Validation)

```mermaid
sequenceDiagram
    participant User
    participant Filter
    participant JwtUtil
    participant Controller

    User->>Filter: Request + Authorization: Bearer JWT
    Filter->>JwtUtil: validateToken(JWT)
    JwtUtil-->>Filter: valid / invalid
    alt valid
        Filter->>Controller: forward request
        Controller-->>User: Response
    else invalid
        Filter-->>User: 401 Unauthorized
    end
```

---

## 🎭 Use Case Diagram

```mermaid
usecaseDiagram
    actor User as "End User"

    User --> (Register New User)
    User --> (Login with Email & Password)
    User --> (Access Secured APIs)

    (Login with Email & Password) --> (Receive JWT Token)
    (Access Secured APIs) --> (JWT Token Validation)
```

---

## 📐 Class Diagram (UML)

```mermaid
classDiagram
    class User {
        +Long id
        +String name
        +String email
        +String password
    }

    class UserRepository {
        <<interface>>
        +findByEmail(String email) : Optional<User>
        +findById(Long id) : Optional<User>
        +save(User user) : User
    }

    class UserDao {
        <<interface>>
        +save(User user) : User
        +findByEmail(String email) : User
        +findById(Long id) : User
    }

    class UserDaoImpl {
        -UserRepository userRepository
        +save(User user) : User
        +findByEmail(String email) : User
        +findById(Long id) : User
    }

    class UserService {
        <<interface>>
        +register(User user) : User
        +getUserById(Long id) : User
        +getUserByEmail(String email) : User
    }

    class UserServicesImpl {
        -UserDao userDao
        +register(User user) : User
        +getUserById(Long id) : User
        +getUserByEmail(String email) : User
    }

    class AuthService {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -JwtUtil jwtUtil
        +login(LoginRequest request) : LoginResponse
    }

    class JwtUtil {
        +generateToken(String email) : String
        +extractEmail(String token) : String
        +isTokenValid(String token, String email) : boolean
    }

    class AuthController {
        -AuthService authService
        +register(User user) : ResponseEntity
        +login(LoginRequest request) : ResponseEntity
    }

    class UserController {
        -UserServicesImpl userService
        +getUserById(Long id) : ResponseEntity
        +getUserByEmail(String email) : ResponseEntity
        +updateUser(Long id, User user) : ResponseEntity
        +deleteUser(Long id) : ResponseEntity
    }

    %% Relationships
    UserDaoImpl ..|> UserDao
    UserServicesImpl ..|> UserService
    UserRepository <.. UserDaoImpl
    UserDao <.. UserServicesImpl
    UserRepository <.. AuthService
    JwtUtil <.. AuthService
    AuthService <.. AuthController
    UserServicesImpl <.. UserController
```

---

## 🗄️ ER Diagram (Database Schema)

```mermaid
erDiagram
    USER {
        BIGINT id PK
        VARCHAR name
        VARCHAR email UK
        VARCHAR password
    }
```

---

## 🚀 Running the Project

### Prerequisites

* Java 17+
* Maven / Gradle
* Local MySQL or PostgreSQL DB

### Steps

1. Clone the repo
2. Configure DB credentials in `application.properties`
3. Run:

   ```bash
   mvn spring-boot:run
   ```
4. Test APIs using Postman or curl

---
