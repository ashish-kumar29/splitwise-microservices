package com.ashish.splitwise.GroupService.Dao;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    public Group save(Group group);
    public Group update(Group group);
    public Optional<Group> findById(Long id);
    public Optional<Group> findByAdmin(Long memberId);
    public List<Group> findAll();
    public void deleteById(Long id);
}
