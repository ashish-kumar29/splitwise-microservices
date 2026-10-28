package com.ashish.splitwise.UserService.DTO;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserNewPassword {
    private String email;
    private String newPassword;
}
