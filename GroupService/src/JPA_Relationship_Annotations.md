
---

# 📖 JPA Relationships Cheat Sheet

This document explains different JPA relationship annotations (`@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`) and commonly used parameters.

---

## 🔹 1. `@OneToOne`

### Use case:

* When one entity is associated with exactly **one** other entity.
* Example: A `User` has **one** `Profile`.

### Example:

```java
@Entity
class User {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id") // foreign key column in User table
    private Profile profile;
}
```

---

## 🔹 2. `@ManyToOne`

### Use case:

* When **many entities** are linked to **one parent**.
* Example: Many `Orders` belong to **one** `User`.

### Example:

```java
@Entity
class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // foreign key column in Order table
    private User user;
}
```

---

## 🔹 3. `@OneToMany`

### Use case:

* When **one entity** has a collection of other entities.
* Example: A `User` has many `Orders`.

### Example:

```java
@Entity
class User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}
```

Here, `mappedBy = "user"` means the **`user` field in Order** owns the relationship (foreign key is in `Order` table).

---

## 🔹 4. `@ManyToMany`

### Use case:

* When multiple entities are linked to multiple others.
* Example: A `Student` can enroll in many `Courses` and each `Course` can have many `Students`.

### Example:

```java
@Entity
class Student {
    @ManyToMany
    @JoinTable(
        name = "student_course", // join table
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```

---

# ⚙️ Common Parameters in Relationships

### 1. `mappedBy`

* Defines **which side owns the relationship**.
* Used in bidirectional relationships.
* Example:

  ```java
  @OneToMany(mappedBy = "user")
  private List<Order> orders;
  ```

### 2. `cascade`

* Defines what operations should cascade from parent to child.
* Common values:

    * `CascadeType.ALL` → Propagate all operations (persist, merge, remove).
    * `CascadeType.PERSIST` → Save child when saving parent.
    * `CascadeType.REMOVE` → Delete child when deleting parent.

### 3. `orphanRemoval`

* If `true`, removes child entities when they are removed from parent’s collection.
* Example:

  ```java
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Order> orders;
  ```

### 4. `fetch`

* Defines when related entities are loaded.
* Options:

    * `FetchType.EAGER` → Always load immediately.
    * `FetchType.LAZY` → Load only when accessed (recommended for performance).

### 5. `@JoinColumn`

* Defines **foreign key column**.
* Example:

  ```java
  @ManyToOne
  @JoinColumn(name = "user_id") // FK in child table
  private User user;
  ```

### 6. `@JoinTable`

* Used in `@ManyToMany` to create an intermediate join table.
* Example shown above in `Student-Course`.

---

✅ **Rule of Thumb**

* Use `@ManyToOne` on the **child** side (where the foreign key actually lives).
* Use `@OneToMany(mappedBy = ...)` on the **parent** side for collections.
* Use `orphanRemoval = true` only if removing a child from a collection should also delete it from DB.
* Always prefer `FetchType.LAZY` unless you absolutely need eager loading.

---


### ⚡ **Default Fetch Types in JPA**

| Relationship Type | Default Fetch Type | Why (Reasoning)                                                                           |
| ----------------- | ------------------ | ----------------------------------------------------------------------------------------- |
| **`@OneToOne`**   | `EAGER`            | Framework assumes if you load one entity, the "one-to-one buddy" is almost always needed. |
| **`@ManyToOne`**  | `EAGER`            | Same assumption: if you fetch an order, you’ll likely need its customer.                  |
| **`@OneToMany`**  | `LAZY`             | Could be huge list → not safe to always load by default.                                  |
| **`@ManyToMany`** | `LAZY`             | Could also be very large (e.g., user-roles, tags).                                        |

---

### ⚠️ Problems With Defaults

* The JPA defaults (`EAGER` for `@OneToOne` and `@ManyToOne`) **often cause performance traps** in real-world apps.
* Example: Fetching 1000 Orders → each one eagerly loads its Customer → **N+1 queries** or one **huge join**.

---

### ✅ Best Practice (What pros do)

* Override defaults:

  ```java
  @OneToOne(fetch = FetchType.LAZY)
  private Profile profile;

  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;
  ```
* Basically: **make everything LAZY unless you have a very strong reason not to.**
* Then control fetching explicitly in queries (`JOIN FETCH`, `EntityGraph`, projections).

---

👉 So in short:

* **Default** = OneToOne & ManyToOne → EAGER, OneToMany & ManyToMany → LAZY.
* **Recommended** = Force all to LAZY, fetch explicitly when needed.

---