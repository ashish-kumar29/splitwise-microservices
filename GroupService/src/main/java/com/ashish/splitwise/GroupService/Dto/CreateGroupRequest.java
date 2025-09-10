package com.ashish.splitwise.GroupService.Dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupRequest {
    private String groupName;
    private String description;
    private Long adminUserId;
}
