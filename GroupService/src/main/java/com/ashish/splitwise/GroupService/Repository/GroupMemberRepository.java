package com.ashish.splitwise.GroupService.Repository;

import com.ashish.splitwise.GroupService.Model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
