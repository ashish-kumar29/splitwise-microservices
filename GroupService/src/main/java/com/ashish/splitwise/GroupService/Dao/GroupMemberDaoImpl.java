package com.ashish.splitwise.GroupService.Dao;


import com.ashish.splitwise.GroupService.Model.GroupMember;
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
    public GroupMember save(GroupMember groupMember) {
        return memberRepository.save(groupMember);
    }

    @Override
    public GroupMember update(GroupMember groupMember) {
        return memberRepository.save(groupMember);
    }

    @Override
    public Optional<GroupMember> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public List<GroupMember> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

}
