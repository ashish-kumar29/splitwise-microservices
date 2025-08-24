package com.ashish.splitwise.UserService.Dao;

import com.ashish.splitwise.UserService.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    User update(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(long id);

}
