
# 📌 ResponseEntity Notes (Spring Boot)

## 🔹 What is ResponseEntity?
- A wrapper for the HTTP **response** in Spring Boot.
- Lets you control:
  - **HTTP Status Code** (200, 201, 204, 400, etc.)
  - **Headers** (Authorization, Location, Custom headers)
  - **Body** (DTO / JSON response)

---

## 🔹 When to Use `ResponseEntity`

| HTTP Method | Common Status Codes | Return Type | Use `ResponseEntity` When… |
|-------------|---------------------|-------------|-----------------------------|
| **GET**     | `200 OK` / `404 Not Found` | DTO (`UserResponse`) | ❌ Not always needed, unless you want `404` or custom headers |
| **POST**    | `201 Created` | `ResponseEntity<UserResponse>` | ✅ Recommended → to return `201 Created` and optionally add `Location` header |
| **PUT / PATCH** | `200 OK` / `204 No Content` | DTO or `ResponseEntity<Void>` | ✅ Use when update can return `204` or needs flexible response |
| **DELETE**  | `204 No Content` / `404 Not Found` | `ResponseEntity<Void>` | ✅ Required → for `204` response with no body |

---

## 🔹 Examples

### ✅ Returning DTO (simple case)
```java
@GetMapping("/{id}")
public UserResponse getUser(@PathVariable Long id) {
    return userService.getUserById(id);
}
````

* Straightforward, defaults to `200 OK`.

---

### ✅ Returning with `ResponseEntity` (flexible case)

```java
@PostMapping
public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
    UserResponse response = userService.createUser(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
}
```

* Returns `201 Created` with body.

---

### ✅ Returning with headers

```java
@PostMapping("/login")
public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    UserResponse response = authService.login(request);
    return ResponseEntity.ok()
            .header("Authorization", response.getJwtToken())
            .body(response);
}
```

---

### ✅ Empty response

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
}
```

---

## 🔹 Summary

* **Use DTO directly** → simple `GET` responses.
* **Use ResponseEntity** → whenever you need:

    * Custom status codes
    * Empty responses (`204`)
    * Custom headers
    * Error handling (`400`, `401`, `403`, etc.)

👉 Rule of Thumb: **For POST, PUT, DELETE → prefer `ResponseEntity`**.
