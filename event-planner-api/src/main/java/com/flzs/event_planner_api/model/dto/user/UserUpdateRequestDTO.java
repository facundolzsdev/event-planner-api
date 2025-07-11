package com.flzs.event_planner_api.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {

    @NotBlank(message = "{NotBlank.user.username}")
    @Size(min = 5, max = 15, message = "{Size.user.username}")
    private String username;

    @Size(min = 8, max = 30, message = "{Size.user.password}") // Can be null if not updated
    private String password;

}