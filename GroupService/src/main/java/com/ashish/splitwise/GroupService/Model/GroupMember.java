package com.ashish.splitwise.GroupService.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "groupMembers")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    @Column(nullable = false, name = "user_name")
    private String userName;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JsonBackReference
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getUserId(){
        return id!=null?id.getUserId():null;
    }
}
