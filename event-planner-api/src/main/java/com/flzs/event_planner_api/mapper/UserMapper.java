package com.flzs.event_planner_api.mapper;

import com.flzs.event_planner_api.model.dto.user.*;
import com.flzs.event_planner_api.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDTO dto, String hashedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(hashedPassword);
        return user;
    }

    public UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    public void updateEntityFromDto(UserUpdateRequestDTO dto, User user) {
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
    }
}
