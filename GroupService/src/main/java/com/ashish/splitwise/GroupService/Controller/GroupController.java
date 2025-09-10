package com.ashish.splitwise.GroupService.Controller;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Group> insertGroup(@RequestBody Group group){
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group group){
        return ResponseEntity.ok(groupService.updateGroup(id, group));
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable Long id){
        return groupService.getGroupById(id);
    }

    @GetMapping("/admin/{adminId}")
    public List<Group> getGroupsByAdmin(@PathVariable Long adminId){
        return groupService.getGroupByAdminUserId(adminId);
    }

    @GetMapping
    public List<Group> getAllGroup(){
        return groupService.getAllGroup();
    }

    @GetMapping("/{id}/admin")
    public GroupMember getAdmin(@PathVariable Long id) throws Exception{
        return groupService.getGroupAdminByGroupId(id);
    }

    @GetMapping("{id}/members")
    public List<GroupMember> getAllMembers(@PathVariable Long id){
        return groupService.getAllMembersByGroupId(id);
    }

    @PostMapping("{groupId}/user/{memberId}")
    public String addMember(@PathVariable Long groupId, @PathVariable Long memberId){
        return groupService.addMemberToGroup(groupId, memberId);
    }
    @DeleteMapping("{groupId}/user/{memberId}")
    public String deleteMember(@PathVariable Long groupId, @PathVariable Long memberId)throws Exception{
        return groupService.deleteMemberFromGroup(groupId, memberId);
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Long id){
        return groupService.deleteGroup(id);
    }

}
