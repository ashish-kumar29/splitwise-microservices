package com.ashish.splitwise.GroupService.Repository;

import com.ashish.splitwise.GroupService.Model.Group;
import com.ashish.splitwise.GroupService.Model.GroupMember;
import com.ashish.splitwise.GroupService.Model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    @Query("select gm.group from GroupMember gm where gm.id.userId = :userId")
    List<Group> findAllByUserId(@Param("userId") Long userId);
}
