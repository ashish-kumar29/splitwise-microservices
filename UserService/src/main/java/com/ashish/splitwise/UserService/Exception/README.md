# Exception Package

This package contains all the custom exception handling classes for the UserService project.  
It ensures that our APIs return meaningful, user-friendly, and consistent error messages instead of default Spring Boot error responses.

## Files

1. **UserNotFoundException.java**
    - Custom exception class.
    - Thrown when a requested user resource is not found in the database.

2. **UserErrorResponse.java**
    - POJO class representing the structure of error messages.
    - Contains fields like:
        - `status` → HTTP status code
        - `message` → Description of the error
        - `timeStamp` → Time of error occurrence

3. **UserExceptionHandler.java**
    - Global exception handler.
    - Annotated with `@ControllerAdvice`.
    - Catches custom exceptions like `UserNotFoundException` and maps them into a `UserErrorResponse`.
    - Ensures consistent error format across all User APIs.

---

## Example Error Response

When requesting a user with an ID that doesn’t exist:
```json
{
  "status": 404,
  "message": "User not found with ID: 10",
  "timeStamp": 1723910400000
}
