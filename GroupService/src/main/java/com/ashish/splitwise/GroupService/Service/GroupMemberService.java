package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.Role;

import java.util.List;

public interface GroupMemberService {
    public GroupMember createMember(GroupMember groupMember);
    public GroupMember getMemberById(Long id);
    public List<GroupMember> getAllMembers();
//    public List<GroupMember> getMemberByGroupId(Long groupId);   Not required as this will will easy using group api
    public Group getGroupByMemberId(Long id);
    public Role getRoleById(Long id);
    public GroupMember updateMemberById(Long id, GroupMember member);
    public String deleteMemberById(Long id);
}
