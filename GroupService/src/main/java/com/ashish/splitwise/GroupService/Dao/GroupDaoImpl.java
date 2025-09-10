package com.ashish.splitwise.GroupService.Dao;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Repository.GroupRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupDaoImpl implements GroupDao{

    private final GroupRepository groupRepository;

    public GroupDaoImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public Optional<Group> findByAdmin(Long memberId) {
        return groupRepository.findByAdminUserId(memberId);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
}
