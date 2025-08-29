package com.ashish.splitwise.UserService.Controller;

import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.LoginResponse;
import com.ashish.splitwise.UserService.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
