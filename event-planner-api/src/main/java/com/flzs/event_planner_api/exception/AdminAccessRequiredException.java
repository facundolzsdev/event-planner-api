package com.flzs.event_planner_api.exception;

import org.springframework.security.access.AccessDeniedException;

public class AdminAccessRequiredException extends AccessDeniedException {
    public AdminAccessRequiredException(String message) {
        super(message);
    }
}