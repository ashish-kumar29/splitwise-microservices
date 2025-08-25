
# 📘 JPARepository Notes

## 🔹 What is `JpaRepository`?

* An interface provided by Spring Data JPA.
* Extends `CrudRepository` and `PagingAndSortingRepository`.
* Gives built-in methods for common database operations.

---

## 🔹 Common Built-in Methods

| Method           | Description                  |
| ---------------- | ---------------------------- |
| `save(entity)`   | Save or update an entity     |
| `findById(id)`   | Find entity by primary key   |
| `findAll()`      | Get all records              |
| `deleteById(id)` | Delete record by primary key |
| `count()`        | Count total records          |
| `existsById(id)` | Check if record exists       |

---

## 🔹 Custom Finder Methods

Spring Data can generate queries based on **method names**.

### ✅ Supported Prefixes

* `findBy...`
* `readBy...`
* `queryBy...`
* `countBy...`
* `existsBy...`

---

### ✅ Examples

#### 1. Single Column

```java
Optional<User> findByEmail(String email);
```

👉 `SELECT * FROM users WHERE email = ?;`

---

#### 2. Multiple Columns (AND)

```java
Optional<User> findByEmailAndPassword(String email, String password);
```

👉 `SELECT * FROM users WHERE email = ? AND password = ?;`

---

#### 3. Multiple Columns (OR)

```java
List<User> findByNameOrEmail(String name, String email);
```

👉 `SELECT * FROM users WHERE name = ? OR email = ?;`

---

#### 4. LIKE / Containing

```java
List<User> findByNameContaining(String keyword);
```

👉 `SELECT * FROM users WHERE name LIKE %keyword%;`

---

#### 5. Sorting

```java
List<User> findByNameOrderByEmailAsc(String name);
```

👉 `SELECT * FROM users WHERE name = ? ORDER BY email ASC;`

---

#### 6. Greater Than / Less Than

```java
List<User> findByIdGreaterThan(Long id);
```

👉 `SELECT * FROM users WHERE id > ?;`

---

## 🔹 Important Rules

1. **Method names must match entity field names**.

    * `findByEmail` ✅ works (field = `email`)
    * `findByMail` ❌ fails (no field `mail`)

2. **If method name doesn’t follow rules → app fails at startup.**

3. **For full freedom in method naming → use `@Query`.**

---

## 🔹 Using `@Query` (Custom Queries)

#### Example 1 – JPQL

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> getUserByEmail(@Param("email") String email);
```

#### Example 2 – Multiple conditions

```java
@Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% AND u.email LIKE %:domain%")
List<User> searchByNameAndEmail(@Param("keyword") String keyword, @Param("domain") String domain);
```

👉 Here, you can name the method anything (`getUserByEmail`, `searchUser`, `banana()`) because the query is explicitly defined.

---

## 🔹 Best Practices

* Use **method name queries** for simple lookups (clean + readable).
* Use **@Query** for complex queries.
* Prefer `Optional<T>` when expecting 0 or 1 result.
* Use `List<T>` when expecting multiple results.

---
