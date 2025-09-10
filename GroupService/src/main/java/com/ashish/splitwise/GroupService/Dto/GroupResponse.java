package com.ashish.splitwise.GroupService.Dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private String description;
    private Long adminUserId;
    private int count;
}
