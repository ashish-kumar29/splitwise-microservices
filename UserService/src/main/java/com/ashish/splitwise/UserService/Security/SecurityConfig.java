package com.ashish.splitwise.UserService.Security;

import com.ashish.splitwise.UserService.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter; // our custom JWT filter
    private final CustomUserDetailsService userDetailsService; // to fetch user data from DB


    public SecurityConfig(JwtAuthenticationFilter authenticationFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = authenticationFilter;
        this.userDetailsService = customUserDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2️⃣ Authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Tells Spring Security how to encode passwords
        authProvider.setPasswordEncoder(passwordEncoder());

        // Tells Spring Security how to fetch user details (via CustomUserDetailsService)
        authProvider.setUserDetailsService(userDetailsService);

        return authProvider;
    }

    // 3️⃣ Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Retrieves AuthenticationManager (needed for login authentication)
        return config.getAuthenticationManager();
    }

    // 4️⃣ Security filter chain → defines how requests are secured
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF (not needed for JWT stateless APIs)

                .authorizeHttpRequests(auth -> auth
                        // Allow login & register APIs without authentication
                        .requestMatchers("/api/user/login", "/api/user/register").permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Add our JWT filter before Spring's default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured filter chain
        return http.build();
    }
}
