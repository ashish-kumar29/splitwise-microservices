package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Dao.GroupDao;
import com.ashish.splitwise.GroupService.Dao.GroupMemberDao;
import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupDao groupDao;
    private final GroupMemberService groupMemberService;

    public GroupServiceImpl(GroupDao groupDao, GroupMemberService groupMemberService) {
        this.groupDao = groupDao;
        this.groupMemberService = groupMemberService;
    }

    @Override
    public Group createGroup(Group group) {
        List<GroupMember> members = group.getMembers() != null ? group.getMembers() : new ArrayList<>();
        boolean hasAdmin = members.stream().anyMatch(m -> m.getRole() == Role.ADMIN);
        if(!hasAdmin){
            throw new IllegalArgumentException("A group must have an admin");
        }
        // Ensure each member has the group reference
        members.forEach(member -> member.setGroup(group));
        group.setMembers(members);
        return groupDao.save(group);
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
    public Group getGroupByAdmin(Long adminId) {

        return groupDao.findByAdmin(adminId).orElseThrow(()-> new RuntimeException("no Group for given Group Member id "+adminId));
    }

    @Override
    public GroupMember getGroupAdminByGroupId(Long id) {
        Group group = getGroupById(id);

        // Fast path: adminId already cached
        if (group.getAdminId() != null) {
            return groupMemberService.getMemberById(group.getAdminId()); // direct DB lookup
        }

        // Fallback: compute from members
        List<GroupMember> members = group.getMembers();
        GroupMember admin = members.stream()
                .filter(m -> m.getRole() == Role.ADMIN)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No admin found in members list"));

        // Cache adminId (points to GroupMember.id)
        if(group.getAdminId() == null){
            group.setAdminId(admin.getId());
            updateGroup(id, group);
        }
        return admin;
    }

    @Override
    public List<GroupMember> getAllMembersByGroupId(Long id) {
        Group group = getGroupById(id);
        return group.getMembers();
    }

//    @Override
//    public String addMemberToGroup(Long groupId, GroupMember groupMember) {
//        Group group = getGroupById(groupId);
//        List<GroupMember> members = group.getMembers();
//        members.add(groupMember);
//        group.setMembers(new ArrayList<>(members));
//        groupDao.update(group);
//        return "Member added successfully";
//    }
//
//    @Override
//    public String deleteMemberFromGroup(Long groupId, GroupMember groupMember) {
//        Group group = getGroupById(groupId);
//        List<GroupMember> members = group.getMembers();
//        members.remove(groupMember);
//        group.setMembers(new ArrayList<>(members));
//        groupDao.update(group);
//        return "Member deleted successfully";
//    }

    @Override
    public String addMemberToGroup(Long groupId, Long memberId) {
        Group group = getGroupById(groupId);
        GroupMember groupMember = groupMemberService.getMemberById(memberId);
        groupMember.setGroup(group);
        List<GroupMember> members = group.getMembers();
        members.add(groupMember);
        group.setMembers(new ArrayList<>(members));
        groupDao.update(group);
        return "Member added successfully";
    }

    @Override
    public String deleteMemberFromGroup(Long groupId, Long memberId) {
        Group group = getGroupById(groupId);
        GroupMember groupMember = groupMemberService.getMemberById(memberId);
        group.getMembers().remove(groupMember);
        groupMember.setGroup(null);
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
}
