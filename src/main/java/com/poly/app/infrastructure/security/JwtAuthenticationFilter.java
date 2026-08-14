package com.poly.app.infrastructure.security;


import com.poly.app.domain.model.Customer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final CustomUserDetailsService customerUserDetailsService;


    @Autowired
    private HttpSession session;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        log.info("Request Path: " + path);
        return path.startsWith("/ws") || path.startsWith("/websocket"); // Bỏ qua WebSocket endpoint
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtilities.getToken(request);
        log.info(token);
        if (token != null && jwtUtilities.validateToken(token)) {
            String id = jwtUtilities.extractUserId(token);
            String username = jwtUtilities.extractUsername(token);
            String userType = jwtUtilities.extractRoleName(token);
            System.out.println(id + userType + username);
            UserDetails userDetails = null;
            userDetails = customerUserDetailsService.loadUserByUsername(username); // CustomerService logic
            System.out.println("userDetails" + userDetails);
            System.out.println("jwtUtilities.validateToken(token)" + jwtUtilities.validateToken(token));
            if (userDetails != null && jwtUtilities.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute("user", userDetails);
            }
        }
        filterChain.doFilter(request, response);
    }

}
