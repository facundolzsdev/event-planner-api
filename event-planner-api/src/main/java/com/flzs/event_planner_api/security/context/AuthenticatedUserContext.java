package com.flzs.event_planner_api.security.context;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility for accessing the currently authenticated user.
 * <p>
 * Provides methods to:
 * - Get the username from the security context.
 * - Verify roles (ADMIN/USER) without database access.
 * </p>
 * <p>
 * Note: Only for non-persistent validations).
 */
@Component
public class AuthenticatedUserContext {

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

}
