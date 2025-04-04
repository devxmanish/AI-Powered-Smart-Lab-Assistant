package com.ailab.assistant.config;

import com.ailab.assistant.security.jwt.JwtUtils;
import com.ailab.assistant.security.services.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public AuthHandshakeInterceptor(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userEmail = extractUserEmailFromToken(request);
        if (userEmail != null) {
            // Load UserDetails using email
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
                attributes.put("userId", userDetailsImpl.getId()); // Store userId
                return true;
            }
        }
        return false; // Reject handshake if the token is invalid
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private String extractUserEmailFromToken(ServerHttpRequest request) {
        String query = request.getURI().getQuery(); // Get query params
        if (query != null && query.contains("token=")) {
            String token = query.split("token=")[1].split("&")[0]; // Extract token
            if (jwtUtils.validateJwtToken(token)) {
                return jwtUtils.getEmailFromJwtToken(token);
            }
        }
        return null;
    }

}
