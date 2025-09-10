package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Dao.GroupMemberDao;
import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
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
    public List<Group> getAllGroupByUserId(Long userId) {
        return groupMemberDao.getAllGroupOfUser(userId);
    }

    @Override
    public GroupMember getMemberById(GroupMemberId id) throws Exception{
        return groupMemberDao.findById(id).orElseThrow(()->  new RuntimeException("No Group with groupId as "+id));
    }

    @Override
    public Role getRoleById(GroupMemberId id) throws Exception {
        return getMemberById(id).getRole();
    }
}
