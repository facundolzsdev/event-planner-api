package com.flzs.event_planner_api.security.exception;

import com.flzs.event_planner_api.exception.AdminAccessRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Centralized exception handling for REST controllers.
 * <p>
 * Translates Java exceptions into standardized HTTP error responses with JSON bodies.
 *
 * <p>Handled exceptions:</p>
 * <ul>
 *   <li>400 - Validation errors ({@code @Valid} failures)</li>
 *   <li>401 - Authentication failures (delegated to {@link CustomAuthenticationEntryPoint})</li>
 *   <li>403 - Authorization failures (role-based access denied)</li>
 *   <li>404 - Resource not found</li>
 *   <li>409 - Business rule violations (e.g. duplicate username)</li>
 * </ul>
 *
 * @see RestControllerAdvice
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //400 - DTOs Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // 404 - Event/User not found or unauthorized
    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // 409 - Conflict (username already exists)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleConflict(IllegalArgumentException ex) {
        if (ex.getMessage().contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    // 403 - Access denied (unauthorized roles)
    @ExceptionHandler({AdminAccessRequiredException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, String>> handleAccessDenied(Exception ex) {
        String message = ex instanceof AdminAccessRequiredException ?
                ex.getMessage() : "Default access denied message";

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "Access denied",
                        "message", message
                ));
    }
}
