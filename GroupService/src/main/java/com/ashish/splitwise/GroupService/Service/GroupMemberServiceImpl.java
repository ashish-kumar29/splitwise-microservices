package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Dao.GroupMemberDao;
import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupMemberServiceImpl implements GroupMemberService{

    private final GroupMemberDao groupMemberDao;

    public GroupMemberServiceImpl(GroupMemberDao groupMemberDao) {
        this.groupMemberDao = groupMemberDao;
    }


    @Override
    public GroupMember createMember(GroupMember groupMember) {
        return groupMemberDao.save(groupMember);
    }

    @Override
    public GroupMember getMemberById(Long id) {
        return groupMemberDao.findById(id).orElseThrow(() -> new RuntimeException("No member Exist with id "+id));
    }

    @Override
    public List<GroupMember> getAllMembers() {
        return groupMemberDao.findAll();
    }

    @Override
    @Transactional
    public Group getGroupByMemberId(Long id) {
        GroupMember member = getMemberById(id);
        Group group = member.getGroup();
        return group;
    }

    @Override
    public Role getRoleById(Long id) {
        return getMemberById(id).getRole();
    }

    @Override
    public GroupMember updateMemberById(Long id, GroupMember member) {
        return groupMemberDao.update(convertToMember(id, member));
    }

    @Override
    public String deleteMemberById(Long id) {
        groupMemberDao.deleteById(id);
        return "Member with Id "+id+" got deleted successfully";
    }


    public GroupMember convertToMember(Long id, GroupMember member){
        GroupMember member1 = getMemberById(id);
        if(member.getUserName() != null){
            member1.setUserName(member.getUserName());
        }
        return member1;
    }
}
