package com.flzs.event_planner_api.service;

import com.flzs.event_planner_api.model.dto.user.*;
import com.flzs.event_planner_api.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {

    void register(RegisterRequestDTO dto);

    void registerAdmin(RegisterRequestDTO dto, User creator);

    UserResponseDTO getAuthenticatedUserProfile();

    UserResponseDTO updateAuthenticatedUser(UserUpdateRequestDTO updateDto);

    void deleteAuthenticatedUser();

    boolean existsByUsername(String username);

    List<UserResponseDTO> getAllUsers(Pageable pageable);

    /**
     * Retrieves the full authenticated {@link User} entity from the database.
     * <p>
     * This method should be used in service or controller layers when user-related
     * persistence operations are required (e.g., creating, updating, or deleting events).
     *
     * @return the authenticated user entity from the database
     * @throws UsernameNotFoundException if the user does not exist in the database
     */
    User getCurrentAuthenticatedUser();
}
