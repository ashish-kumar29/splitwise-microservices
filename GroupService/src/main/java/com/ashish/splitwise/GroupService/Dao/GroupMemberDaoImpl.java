package com.ashish.splitwise.GroupService.Dao;


import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
import com.ashish.splitwise.GroupService.Repository.GroupMemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupMemberDaoImpl implements GroupMemberDao{

    private final GroupMemberRepository memberRepository;

    public GroupMemberDaoImpl(GroupMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public List<Group> getAllGroupOfUser(Long userId) {
        return memberRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<GroupMember> findById(GroupMemberId id) {
        return memberRepository.findById(id);
    }
}
