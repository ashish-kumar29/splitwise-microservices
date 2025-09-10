package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    public Group createGroup(Group group);
    public Group getGroupById(Long id);
    public List<Group> getAllGroup();
    public List<Group> getGroupByAdminUserId(Long adminUserId);
    public GroupMember getGroupAdminByGroupId(Long id) throws Exception;
    public List<GroupMember> getAllMembersByGroupId(Long id);
    public String addMemberToGroup(Long groupId, Long userId);
    public String deleteMemberFromGroup(Long groupId, Long userId) throws Exception;
    public Group updateGroup(Long id, Group group);
    public String deleteGroup(Long id);
}
