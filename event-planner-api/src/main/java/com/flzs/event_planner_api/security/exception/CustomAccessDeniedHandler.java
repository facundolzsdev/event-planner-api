package com.flzs.event_planner_api.security.exception;

import com.flzs.event_planner_api.exception.AdminAccessRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Handles 403 Forbidden responses for unauthorized access attempts.
 * Specifically formats admin-related permission denials.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        String message;
        if (ex instanceof AdminAccessRequiredException) {
            message = ex.getMessage(); // Specific message
        } else {
            message = "You do not have permission to access this resource."; // Generic Message
        }

        Map<String, String> body = Map.of(
                "error", "Access Denied",
                "message", message
        );

        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}