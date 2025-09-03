package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    public Group createGroup(Group group);
    public Group getGroupById(Long id);
    public List<Group> getAllGroup();
    public Group getGroupByAdmin(Long adminId);
    public GroupMember getGroupAdminByGroupId(Long id);
    public List<GroupMember> getAllMembersByGroupId(Long id);
//    public String addMemberToGroup(Long groupId, GroupMember groupMember);
//    public String deleteMemberFromGroup(Long groupId, GroupMember groupMember);
    public String addMemberToGroup(Long groupId, Long memberId);
    public String deleteMemberFromGroup(Long groupId, Long memberId);
    public Group updateGroup(Long id, Group group);
    public String deleteGroup(Long id);
}
