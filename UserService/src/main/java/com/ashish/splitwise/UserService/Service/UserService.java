package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.DTO.LoginRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationRequest;
import com.ashish.splitwise.UserService.DTO.UserRegistrationResponse;
import com.ashish.splitwise.UserService.Model.User;

import java.util.List;

public interface UserService {
    public User createUser(User user);
    public User updateUser(User user, Long id);
    public Integer getAge(Long id);
    public User getUserById(Long id);
    public User getUserByEmail(String email);
    public List<User> findAllUser();
    public void deleteUser(long id);
    public UserRegistrationRequest getUserInfo(long id);
    public UserRegistrationResponse registerUser(UserRegistrationRequest request);
    public String loginUser(LoginRequest request);
}
