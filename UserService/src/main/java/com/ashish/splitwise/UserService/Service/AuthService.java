package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.LoginResponse;
import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Repository.UserRepository;
import com.ashish.splitwise.UserService.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("Invalid Email: "+request.getEmail()));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Credential");
        }

        String token = JwtUtil.generateToken(request.getEmail());
        return new LoginResponse(token);
    }
}
