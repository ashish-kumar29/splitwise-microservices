package com.ashish.splitwise.UserService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserRegistrationResponse {
    private Long id;
    private String name;
    private String email;
}
