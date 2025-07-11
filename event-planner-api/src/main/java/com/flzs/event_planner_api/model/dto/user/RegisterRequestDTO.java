package com.flzs.event_planner_api.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank(message = "{NotBlank.user.username}")
    @Size(min = 5, max = 15, message = "{Size.user.username}")
    private String username;

    @NotBlank(message = "{NotBlank.user.password}")
    @Size(min = 8, max = 30, message = "{Size.user.password}")
    private String password;

}
