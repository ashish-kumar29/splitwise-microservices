package com.ashish.splitwise.UserService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequest {
    private String email;
    private String password;
}
