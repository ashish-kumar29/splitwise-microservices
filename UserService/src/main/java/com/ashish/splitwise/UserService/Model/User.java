package com.ashish.splitwise.UserService.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobNo;

    @Column(nullable = false)
    private String password;

    private Character gender;

    private Integer YearOfBirth;

    private String sport;

    private Integer weight;

    private String city;

    private LocalDateTime createdAt = LocalDateTime.now();

}
