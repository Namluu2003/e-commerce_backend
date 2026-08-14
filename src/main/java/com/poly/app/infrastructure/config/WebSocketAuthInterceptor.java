package com.poly.app.infrastructure.config;

import com.poly.app.infrastructure.security.CustomUserDetailsService;
import com.poly.app.infrastructure.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            // Explicit type checking instead of pattern matching
            if (request instanceof ServletServerHttpRequest) {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                HttpServletRequest httpRequest = servletRequest.getServletRequest();

                // Extensive logging
                log.info("WebSocket Handshake Attempt");
                log.info("Request URL: {}", httpRequest.getRequestURL());

                String authHeader = httpRequest.getHeader("Authorization");
                log.info("Authorization Header: {}", authHeader);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);

                    // Validate token
                    if (validateToken(token)) {
                        log.info("WebSocket Authentication Successful");
                        return true;
                    } else {
                        log.warn("WebSocket Token Validation Failed");
                    }
                } else {
                    log.warn("No Bearer Token Found");
                }
            }
        } catch (Exception e) {
            log.error("WebSocket Handshake Error", e);
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private boolean validateToken(String token) {
        try {
            // Implement your actual token validation logic
            // This might involve:
            // 1. Checking token expiration
            // 2. Verifying token signature
            // 3. Validating user existence
            return true; // Replace with actual validation
        } catch (Exception e) {
            log.error("Token Validation Error", e);
            return false;
        }
    }
}