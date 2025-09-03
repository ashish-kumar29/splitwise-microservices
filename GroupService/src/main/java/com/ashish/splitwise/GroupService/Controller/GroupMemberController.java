package com.ashish.splitwise.GroupService.Controller;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
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

    @PostMapping
    public ResponseEntity<GroupMember> insertGroupMember(@RequestBody GroupMember groupMember){
        return ResponseEntity.ok(memberService.createMember(groupMember));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMember> updateGroupMember(@PathVariable Long id, @RequestBody GroupMember groupMember){
        return ResponseEntity.ok(memberService.updateMemberById(id, groupMember));
    }

    @GetMapping("/{id}")
    public GroupMember getGroupMemberById(@PathVariable Long id){
        return memberService.getMemberById(id);
    }

    @GetMapping
    public List<GroupMember> getAllGroupMembers(){
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}/group")
    public Group getGroupByMemberId(@PathVariable Long id){
        return memberService.getGroupByMemberId(id);
    }

    @GetMapping("/{id}/role")
    public Role getRoleByMemberId(@PathVariable Long id){
        return memberService.getRoleById(id);
    }


    @DeleteMapping("/{id}")
    public String deleteGroupMember(@PathVariable Long id){
        return memberService.deleteMemberById(id);
    }


}
