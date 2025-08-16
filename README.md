
# 💸 Splitwise Clone (Microservices Architecture)

A **Splitwise-like application** built using **Spring Boot microservices**, designed to manage expenses, groups, and balances among users.  
This project is structured into independent microservices such as **User Service**, **Group Service**, and **Expense Service** for scalability and clean separation of concerns.

---

## 🚀 Tech Stack
- **Backend:** Java 17, Spring Boot
- **Database:** MySQL
- **ORM & Utilities:** Spring Data JPA, Hibernate
- **Lombok:** For reducing boilerplate code (@Getter, @Setter, @Builder, etc.)
- **Build Tool:** Maven
- **Version Control:** Git, GitHub

---

## 📂 Project Structure
```

Splitwise/
├── UserService/       # Manages user-related operations
├── GroupService/      # (Planned) Handles group creation & management
├── ExpenseService/    # (Planned) Manages expenses, splits & settlements
└── README.md          # Main project overview

````

---
## 🔧 Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Splitwise.git
````

2. Navigate into a microservice (e.g., UserService):

   ```bash
   cd Splitwise/UserService
   ```
3. Update `application.properties` with your local MySQL credentials:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/splitwise_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

---
