package com.ashish.splitwise.UserService.DTO;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ForgetPasswordResponse {
    private String name;
    private String email;
    private String message;
}
