package com.ashish.splitwise.UserService.Service;


import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationResponse;
import com.ashish.splitwise.UserService.Repository.UserRepository;
import com.ashish.splitwise.UserService.Security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServicesImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRegistration(){
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Ashish");
        request.setEmail("ash@kumar.com");
        request.setMobNo("1234567548987");
        request.setPassword("password");

        UserRegistrationResponse response = userService.registerUser(request);

        assertNotNull(response.getId());
        assertEquals("Ashsdddfish", response.getName());
    }
    @Test
    void testUserLogin(){
        LoginRequest request = new LoginRequest();
        request.setEmail("ash@kumar.com");
        request.setPassword("password");

        String token = userService.loginUser(request);
        String email = JwtUtil.extractEmail(token);
        assertEquals("ash@kumar.com", email);
    }
}
