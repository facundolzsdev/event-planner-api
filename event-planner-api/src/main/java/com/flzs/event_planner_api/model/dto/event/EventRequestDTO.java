package com.flzs.event_planner_api.model.dto.event;

import com.flzs.event_planner_api.model.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequestDTO {

    @NotBlank(message = "{NotBlank.event.title}")
    @Size(max = 75, message = "{Size.event.title}")
    private String title;

    @Size(max = 255, message = "{Size.event.description}")
    private String description;

    @NotNull(message = "{NotNull.event.type}")
    private EventType type;

    @NotNull(message = "{NotNull.event.importance}")
    private Importance importance;
}
