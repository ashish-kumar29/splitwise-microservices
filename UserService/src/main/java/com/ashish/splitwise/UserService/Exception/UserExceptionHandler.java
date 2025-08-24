package com.ashish.splitwise.UserService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    ResponseEntity<UserErrorResponse> handleUserException(Exception exc){
        UserErrorResponse errorResponse = new UserErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<UserErrorResponse> handleUserException(UserNotFoundException exc){
        UserErrorResponse errorResponse = new UserErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
