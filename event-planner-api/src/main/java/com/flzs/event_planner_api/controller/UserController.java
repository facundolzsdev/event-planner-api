package com.flzs.event_planner_api.controller;

import com.flzs.event_planner_api.model.dto.user.*;
import com.flzs.event_planner_api.model.entity.User;
import com.flzs.event_planner_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";
    private static final String USER_REGISTERED_MSG = "User successfully registered";
    private static final String ADMIN_CREATED_MSG = "Admin successfully created by ";

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a standard user account using the provided credentials."
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(USER_REGISTERED_MSG);
    }

    @Operation(
            summary = "Create new admin",
            description = "Creates an admin user. This endpoint is intended for administrators."
    )
    @PostMapping("/admin")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody RegisterRequestDTO dto) {
        User creator = userService.getCurrentAuthenticatedUser();
        userService.registerAdmin(dto, creator);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ADMIN_CREATED_MSG + creator.getUsername());
    }

    @Operation(
            summary = "Get authenticated user's profile",
            description = "Returns the profile data of the currently authenticated user."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser() {
        UserResponseDTO userDto = userService.getAuthenticatedUserProfile();
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Update authenticated user's profile",
            description = "Updates the profile information of the currently authenticated user."
    )
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateAuthenticatedUser(
            @Valid @RequestBody UserUpdateRequestDTO updateDto) {

        UserResponseDTO updatedUser = userService.updateAuthenticatedUser(updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete authenticated user",
            description = "Deletes the account of the currently authenticated user."
    )
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAuthenticatedUser() {
        userService.deleteAuthenticatedUser();
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all users (admin only)",
            description = "Returns a paginated list of all registered users. This endpoint is intended for administrators."
    )
    @GetMapping("/admin")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
