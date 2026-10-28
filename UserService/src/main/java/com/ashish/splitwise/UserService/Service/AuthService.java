package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.DTO.*;
import com.ashish.splitwise.UserService.Dao.UserDao;
import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Repository.UserRepository;
import com.ashish.splitwise.UserService.Security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDao userDao;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserDao userDao, UserService userService, AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = JwtUtil.generateToken(authentication.getName());
        return new LoginResponse(token);
    }


    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if(userDao.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already present");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobNo(request.getMobNo());
        user.setPassword(request.getPassword());

        User createdUser = userService.createUser(user);
        return new UserRegistrationResponse(createdUser.getId(), createdUser.getName(), createdUser.getEmail());
    }

    public ForgetPasswordResponse resetPassword(UserNewPassword userNewPassword) {
        User user = userDao.findByEmail(userNewPassword.getEmail()).orElseThrow(()->new RuntimeException("User not found for given email id"));
        user.setPassword(userNewPassword.getNewPassword());
        User updatedUser = userService.createUser(user);
        return new ForgetPasswordResponse(user.getName(), user.getEmail(), "Pawword got updated successfully");
    }
}
