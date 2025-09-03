package com.ashish.splitwise.GroupService.Dao;

import com.ashish.splitwise.GroupService.Model.GroupMember;

import java.util.List;
import java.util.Optional;

public interface GroupMemberDao {
    public GroupMember save(GroupMember groupMember);
    public GroupMember update(GroupMember groupMember);
    public Optional<GroupMember> findById(Long id);
    public List<GroupMember> findAll();
    public void deleteById(Long id);
}
