package com.ashish.splitwise.UserService.Security;

import com.ashish.splitwise.UserService.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // This ensures filter is executed only once per request

    @Autowired
    private JwtUtil jwtUtil;  // Utility class for generating/validating JWT tokens

    @Autowired
    private CustomUserDetailsService userService;
    // Service that fetches user details (email, password, roles) from DB

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Extract Authorization header from the incoming request
        String authHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        // 2️⃣ Check if header is present and starts with "Bearer "
        // Example: "Authorization: Bearer eyJhbGciOiJIUzI1..."
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);     // Remove "Bearer " prefix → get pure token
            email = jwtUtil.extractEmail(token); // Extract email (subject) from token
        }

        // 3️⃣ If we extracted email AND SecurityContext is still empty (not authenticated yet)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from DB using email
            var userDetail = userService.loadUserByUsername(email);

            // Validate token against user information
            if (jwtUtil.isTokenValid(token, email)) {
                // 4️⃣ Create Authentication object
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetail,            // Principal (user identity)
                                null,                  // No password required here
                                userDetail.getAuthorities() // Roles/permissions
                        );

                // Attach additional request-specific details (like IP, session id)
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 5️⃣ Store authentication in SecurityContext
                // After this step → Spring Security knows this request is authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 6️⃣ Pass request forward in filter chain (go to next filter/controller)
        filterChain.doFilter(request, response);
    }
}
