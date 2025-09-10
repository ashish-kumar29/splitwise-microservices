package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
import com.ashish.splitwise.GroupService.Model.Role;

import java.util.List;

public interface GroupMemberService {
    public List<Group> getAllGroupByUserId(Long userId);
    public GroupMember getMemberById(GroupMemberId id) throws Exception;
    public Role getRoleById(GroupMemberId id) throws Exception;
}
