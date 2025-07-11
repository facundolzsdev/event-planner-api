package com.flzs.event_planner_api.model.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private String role;
}
