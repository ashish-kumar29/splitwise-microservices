package com.ashish.splitwise.UserService.Controller;

import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.LoginResponse;
import com.ashish.splitwise.UserService.DTO.UserRegistrationRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationResponse;
import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Service.UserServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    public UserServicesImpl userServices;

    @Autowired
    public UserController(UserServicesImpl userServices){
        this.userServices = userServices;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id){
        return userServices.getUserById(id);
    }
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userServices.getUserByEmail(email);
    }

    @GetMapping
    public List<User> getAllUser(){
        return userServices.findAllUser();
    }
    @PostMapping
    public User insertUser(@RequestBody User user){
        return userServices.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable long id){
        return userServices.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id){
        userServices.deleteUser(id);
    }

    @GetMapping("/info/{id}")
    public UserRegistrationRequest getBasicInfo(@PathVariable long id){
        return userServices.getUserInfo(id);
    }

    @GetMapping("/age/{id}")
    public Integer getAge(@PathVariable long id){
        return userServices.getAge(id);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerNewUser(@RequestBody UserRegistrationRequest registrationRequest){
        return ResponseEntity.ok(userServices.registerUser(registrationRequest));
    }


}
