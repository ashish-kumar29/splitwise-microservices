package com.ashish.splitwise.UserService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String mobNo;
    private String password;
}
