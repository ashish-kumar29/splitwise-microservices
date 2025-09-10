package com.ashish.splitwise.GroupService.Controller;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
import com.ashish.splitwise.GroupService.Model.Role;
import com.ashish.splitwise.GroupService.Service.GroupMemberService;
import com.ashish.splitwise.GroupService.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groupMember")
public class GroupMemberController {

    private final GroupMemberService memberService;

    public GroupMemberController(GroupMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/id")
    public GroupMember getGroupMemberById(@RequestBody GroupMemberId id) throws Exception{
        return memberService.getMemberById(id);
    }

    @GetMapping("/role")
    public Role getRoleByMemberId(@RequestBody GroupMemberId id) throws  Exception{
        return memberService.getRoleById(id);
    }

    @GetMapping("/user/{id}")
    public List<Group> getAllGroupsByUserId(@PathVariable Long userId){
        return memberService.getAllGroupByUserId(userId);
    }


}
