package com.ashish.splitwise.ExpenseService.Client;

import com.ashish.splitwise.ExpenseService.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userService", path = "/api/user")
public interface UserClient {

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable Long id);
}
