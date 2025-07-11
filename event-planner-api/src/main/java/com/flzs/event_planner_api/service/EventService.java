package com.flzs.event_planner_api.service;

import com.flzs.event_planner_api.model.dto.event.*;
import com.flzs.event_planner_api.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EventService {

    EventResponseDTO getEventByIdForUser(Long id, User user);

    List<EventResponseDTO> getAllEventsForUser(User user);

    void createEvent(EventRequestDTO dto, User user);

    void updateEvent(Long eventId, EventRequestDTO dto, User user);

    void deleteEvent(Long eventId, User user);

    List<EventResponseDTO> getAllEvents(Pageable pageable);

}
