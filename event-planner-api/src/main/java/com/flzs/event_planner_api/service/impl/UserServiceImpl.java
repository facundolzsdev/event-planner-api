package com.flzs.event_planner_api.service.impl;

import com.flzs.event_planner_api.exception.AdminAccessRequiredException;
import com.flzs.event_planner_api.mapper.UserMapper;
import com.flzs.event_planner_api.model.dto.user.*;
import com.flzs.event_planner_api.model.entity.User;
import com.flzs.event_planner_api.model.enums.Role;
import com.flzs.event_planner_api.repository.UserRepository;
import com.flzs.event_planner_api.service.UserService;
import com.flzs.event_planner_api.security.context.AuthenticatedUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USERNAME_EXISTS_MSG = "Username already exists";
    private static final String ADMIN_ACCESS_MSG = "Only admins can create other admins";
    private static final String USER_NOT_FOUND = "User not found with username: ";
    private static final String ADMIN_ACCESS_REQUIRED = "Only admins can access all users";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticatedUserContext authUserContext;

    @Transactional
    @Override
    public void register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException(USERNAME_EXISTS_MSG);
        }
        User user = userMapper.toEntity(dto, passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void registerAdmin(RegisterRequestDTO dto, User creator) {
        if (!creator.getRole().equals(Role.ADMIN)) {
            throw new AdminAccessRequiredException(ADMIN_ACCESS_MSG);
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException(USERNAME_EXISTS_MSG);
        }

        User admin = userMapper.toEntity(dto, passwordEncoder.encode(dto.getPassword()));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO getAuthenticatedUserProfile() {
        User currentUser = this.getCurrentAuthenticatedUser();
        return userMapper.toDto(currentUser);
    }

    @Transactional
    @Override
    public UserResponseDTO updateAuthenticatedUser(UserUpdateRequestDTO updateDto) {
        User currentUser = getCurrentAuthenticatedUser();

        if (!currentUser.getUsername().equals(updateDto.getUsername())) {
            if (userRepository.existsByUsername(updateDto.getUsername())) {
                throw new IllegalArgumentException(USERNAME_EXISTS_MSG);
            }
        }

        userMapper.updateEntityFromDto(updateDto, currentUser);
        if (updateDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        return userMapper.toDto(currentUser);
    }

    @Transactional
    @Override
    public void deleteAuthenticatedUser() {
        User currentUser = getCurrentAuthenticatedUser();
        userRepository.delete(currentUser);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDTO> getAllUsers(Pageable pageable) {
        if (!authUserContext.isAdmin()) {
            throw new AdminAccessRequiredException(ADMIN_ACCESS_REQUIRED);
        }

        return userRepository.findAll(pageable)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
    }

}
