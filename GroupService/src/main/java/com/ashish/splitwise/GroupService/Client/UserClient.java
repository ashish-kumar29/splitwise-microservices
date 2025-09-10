package com.ashish.splitwise.GroupService.Client;

import com.ashish.splitwise.GroupService.Dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="UserService", path="/api/user")
public interface UserClient {

    @GetMapping("/{id}")
    UserDto getUser(@PathVariable Long id);
}
