package com.flzs.event_planner_api.model.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flzs.event_planner_api.model.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy/MM/dd - HH:mm:ss")
    private LocalDateTime createdAt;
    private EventType type;
    private Importance importance;
}
