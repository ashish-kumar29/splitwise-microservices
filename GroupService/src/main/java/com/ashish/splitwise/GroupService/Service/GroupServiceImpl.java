package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Client.UserClient;
import com.ashish.splitwise.GroupService.Dao.GroupDao;
import com.ashish.splitwise.GroupService.Dao.GroupMemberDao;
import com.ashish.splitwise.GroupService.Dto.UserDto;
import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
import com.ashish.splitwise.GroupService.Model.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupDao groupDao;
    private final GroupMemberService groupMemberService;
    private final UserClient user;

    public GroupServiceImpl(GroupDao groupDao, GroupMemberService groupMemberService, UserClient user) {
        this.groupDao = groupDao;
        this.groupMemberService = groupMemberService;
        this.user = user;   
    }

    @Override
    public Group createGroup(Group group) {
        Long adminUserId = group.getAdminUserId();
        if(adminUserId == null){
            throw new IllegalArgumentException("A group must have an admin");
        }
        Group savedGroup = groupDao.save(group);
//        UserDto userInfo = user.getUser(adminUserId);

        // Create GroupMember
        GroupMember member1 = convertToMember(savedGroup, adminUserId, Role.ADMIN, "ADMIN USER");
        List<GroupMember> members = savedGroup.getMembers();
        members = members == null? new ArrayList<>():members;
        members.add(member1);
        savedGroup.setMembers(members);
        return groupDao.save(savedGroup);
    }

    @Override
    public Group getGroupById(Long id) {
        return groupDao.findById(id).orElseThrow(()->  new RuntimeException("No Group with groupId as "+id));
    }

    @Override
    public List<Group> getAllGroup() {
        return groupDao.findAll();
    }

    @Override
    public List<Group> getGroupByAdminUserId(Long adminUserId) {
        List<Group> groups = groupMemberService.getAllGroupByUserId(adminUserId);
        return groups.stream()
                .filter(g -> adminUserId.equals(g.getAdminUserId()))
                .toList();
    }

    @Override
    public GroupMember getGroupAdminByGroupId(Long id) throws Exception{
        Group group = getGroupById(id);

        // Fast path: adminId already cached
        if (group.getAdminUserId() != null) {
            return groupMemberService.getMemberById(new GroupMemberId(group.getId(), group.getAdminUserId())); // direct DB lookup
        }
        return null;
    }

    @Override
    public List<GroupMember> getAllMembersByGroupId(Long id) {
        Group group = getGroupById(id);
        return group.getMembers();
    }
    @Override
    public String addMemberToGroup(Long groupId, Long userId) {
        Group group = getGroupById(groupId);
        GroupMember groupMember = convertToMember(group, userId, Role.MEMBER, "member");
        group.getMembers().add(groupMember);
        groupDao.update(group);
        return "Member added successfully";
    }

    @Override
    public String deleteMemberFromGroup(Long groupId, Long id) throws Exception{
        Group group = getGroupById(groupId);
        GroupMember groupMember = groupMemberService.getMemberById(new GroupMemberId(group.getId(), id));
        group.getMembers().remove(groupMember);
        groupDao.update(group);
        return "Member deleted successfully";
    }

    @Override
    public Group updateGroup(Long id, Group group) {
        group = convertToGroup(group, id);
        return groupDao.update(group);
    }

    @Override
    public String deleteGroup(Long id) {
        groupDao.deleteById(id);
        return "Group deleted successfully";
    }

    public Group convertToGroup(Group group, Long id){
        Group group1 = getGroupById(id);
        if(group.getGroupName() != null){
            group1.setGroupName(group.getGroupName());
        }
        if(group.getDescription() != null){
            group1.setDescription(group.getDescription());
        }
        return group1;
    }
    public GroupMember convertToMember(Group group, Long userId, Role role, String username){
        GroupMember member1 = GroupMember.builder()
                .id(new GroupMemberId(group.getId(), userId))
                .role(role)
                .group(group)
                .userName(username)
                .build();
        return member1;
    }

}
