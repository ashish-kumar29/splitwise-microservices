package com.ashish.splitwise.GroupService.Dao;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;

import java.util.List;
import java.util.Optional;

public interface GroupMemberDao {
    public List<Group> getAllGroupOfUser(Long userId);
    public Optional<GroupMember> findById(GroupMemberId id);
}
