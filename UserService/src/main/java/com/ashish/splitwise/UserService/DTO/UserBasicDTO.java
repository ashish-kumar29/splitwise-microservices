package com.ashish.splitwise.UserService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserBasicDTO {
    private Long id;
    private String email;
    private String mobNo;
    private String password;
}
