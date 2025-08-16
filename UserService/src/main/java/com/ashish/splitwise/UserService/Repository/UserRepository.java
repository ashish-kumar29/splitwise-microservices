package com.ashish.splitwise.UserService.Repository;

import com.ashish.splitwise.UserService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
