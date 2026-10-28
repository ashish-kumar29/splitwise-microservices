package com.ashish.splitwise.ExpenseService.Client;


import com.ashish.splitwise.ExpenseService.DTO.GroupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "groupService", path = "/api/group")
public interface GroupClient {
    @GetMapping("/{id}")
    GroupDTO getGroupById(@PathVariable Long id);
}
