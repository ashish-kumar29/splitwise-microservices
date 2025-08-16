package com.ashish.splitwise.UserService.Config;


import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DummyDataLoder {

    @Bean
    CommandLineRunner loadData(UserRepository userRepository){
        return args -> {
            if (userRepository.count() == 0) { // avoid duplicate inserts on restart
                User user = User.builder()
                        .name("Ashish")
                        .email("ashish@test.com")
                        .password("12345")
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(user);
                System.out.println("Dummy users inserted!");
            }
        };
    }
}
