package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.DTO.UserBasicDTO;
import com.ashish.splitwise.UserService.Exception.UserNotFoundException;
import com.ashish.splitwise.UserService.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);
    public User updateUser(User user, Long id);
    public Integer getAge(Long id);
    public User getUserById(Long id);
    public User getUserByEmail(String email);
    public List<User> findAllUser();
    public void deleteUser(long id);
    public UserBasicDTO getUserInfo(long id);
}
