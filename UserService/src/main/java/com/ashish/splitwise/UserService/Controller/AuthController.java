package com.ashish.splitwise.UserService.Controller;

import com.ashish.splitwise.UserService.DTO.*;
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


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerNewUser(@RequestBody UserRegistrationRequest registrationRequest){
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @PostMapping("/forgetPwd")
    public ResponseEntity<ForgetPasswordResponse> forgetPwd(@RequestBody UserNewPassword userNewPassword){
        ForgetPasswordResponse response = authService.resetPassword(userNewPassword);
        return ResponseEntity.ok(response);
    }

}
