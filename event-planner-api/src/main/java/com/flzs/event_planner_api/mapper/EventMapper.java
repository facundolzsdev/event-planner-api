package com.flzs.event_planner_api.mapper;

import com.flzs.event_planner_api.model.dto.event.*;
import com.flzs.event_planner_api.model.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public Event toEntity(EventRequestDTO dto, User user) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setCreatedAt(LocalDateTime.now());
        event.setType(dto.getType());
        event.setImportance(dto.getImportance());
        event.setUser(user);
        return event;
    }

    public EventResponseDTO toDto(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setType(event.getType());
        dto.setImportance(event.getImportance());
        return dto;
    }

    public void updateEntityFromDto(EventRequestDTO dto, Event event) {
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setType(dto.getType());
        event.setImportance(dto.getImportance());
    }

}
