
### **High-Level Architecture (UserService)**

```
        +-------------------+
        |      Client       |
        | (Postman/Frontend)|
        +---------+---------+
                  |
                  v
        +-------------------+
        |   Controller      |   <-- AuthController / UserController
        +---------+---------+
                  |
                  v
        +-------------------+
        |     Service       |   <-- AuthService / UserServicesImpl
        +---------+---------+
                  |
                  v
        +-------------------+
        |      DAO          |   <-- UserDao / UserDaoImpl
        +---------+---------+
                  |
                  v
        +-------------------+
        |   Repository      |   <-- UserRepository (JPA)
        +---------+---------+
                  |
                  v
        +-------------------+
        |      Database     |   <-- users table
        +-------------------+
```

---

### **With Security Flow (Login + JWT)**

```
 Client (Login Request)
        |
        v
  +-----------------+        +------------------+
  | AuthController  | -----> |   AuthService    |
  +-----------------+        +------------------+
                                   |
                                   v
                           +------------------+
                           | UserRepository   |
                           +------------------+
                                   |
                                   v
                                Database
                                   |
                                   v
                           +------------------+
                           |   JwtUtil        | --> generate JWT
                           +------------------+
                                   |
                                   v
                            LoginResponse (token)
```

---

### **With Security (Every Request After Login)**

```
Client (API Call with JWT)
        |
        v
  +---------------------+
  | JwtAuthFilter       | <-- checks token via JwtUtil
  +---------------------+
        |
        v
  +---------------------+
  | SecurityContext     | <-- stores authenticated user
  +---------------------+
        |
        v
  +---------------------+
  | Controller Layer    |
  +---------------------+
        |
        v
  (normal flow to Service → DAO → Repository → DB)
```
