package com.ashish.splitwise.UserService.Service;

import com.ashish.splitwise.UserService.Model.CustomUserDetail;
import com.ashish.splitwise.UserService.Model.User;
import com.ashish.splitwise.UserService.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " Email is not valid"));
        return new CustomUserDetail(user);
    }
}
