

### 1️⃣ `PasswordEncoder`

* **Why?** → When you store passwords in the DB, they should be **hashed** (never plain text).
* **Use** → This bean ensures Spring Security knows *how to hash and compare passwords*.
* `BCryptPasswordEncoder` = most commonly used hashing algorithm.

📌 Example:

* DB has hashed password = `$2a$10$y1uz...`
* User enters raw password `"hello123"`
* Spring Security does → `BCrypt.hash("hello123")` and compares with DB.

---

### 2️⃣ `DaoAuthenticationProvider`

* **Why?** → It’s the link between **Spring Security** and your **database (via UserDetailsService)**.
* **Use**:

    * Tells Spring how to fetch a user (via your `CustomUserDetailsService`).
    * Tells Spring how to check the password (via the `PasswordEncoder`).
* Without this, Spring wouldn’t know how to authenticate against your custom table/entity.

---

### 3️⃣ `AuthenticationManager`

* **Why?** → It’s the central object that **performs authentication** in Spring.

* **Use**:

    * You inject this into your `AuthController` for `/login`.

    * When a login request comes, you call:

      ```java
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, password)
      );
      ```

    * This internally triggers:

        * `DaoAuthenticationProvider` → which calls
        * `CustomUserDetailsService` → fetch user from DB
        * `PasswordEncoder` → match password

* If authentication fails → throws exception.

* If authentication succeeds → returns an `Authentication` object (authenticated user).

---

### ⚡ Without these beans:

* You couldn’t use Spring Security with your own DB.
* Spring wouldn’t know how to **fetch user** (UserDetailsService), or **verify password** (PasswordEncoder).
* `AuthenticationManager` wouldn’t be available for your login API.

---

👉 So in short:

* **PasswordEncoder** → defines how to hash/verify passwords.
* **DaoAuthenticationProvider** → glues DB + Spring Security auth system.
* **AuthenticationManager** → the “boss” that actually authenticates a login request.
