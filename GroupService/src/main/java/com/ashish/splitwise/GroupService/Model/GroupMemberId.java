package com.ashish.splitwise.GroupService.Model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberId implements Serializable {

    private Long groupId;
    private Long userId;
}
