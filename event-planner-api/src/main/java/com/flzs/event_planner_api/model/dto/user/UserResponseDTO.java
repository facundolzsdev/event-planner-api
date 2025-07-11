package com.flzs.event_planner_api.model.dto.user;

import com.flzs.event_planner_api.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private Role role;
}
