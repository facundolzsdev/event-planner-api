package com.flzs.event_planner_api.service.impl;

import com.flzs.event_planner_api.exception.AdminAccessRequiredException;
import com.flzs.event_planner_api.mapper.EventMapper;
import com.flzs.event_planner_api.model.dto.event.*;
import com.flzs.event_planner_api.model.entity.*;
import com.flzs.event_planner_api.repository.EventRepository;
import com.flzs.event_planner_api.service.EventService;
import com.flzs.event_planner_api.security.context.AuthenticatedUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String EVENT_NOT_FOUND = "Event not found with id: ";
    private static final String ADMIN_ACCESS_REQUIRED = "Only admins can access all events";

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AuthenticatedUserContext authUserContext;

    @Transactional(readOnly = true)
    @Override
    public EventResponseDTO getEventByIdForUser(Long id, User user) {
        return eventRepository.findByIdAndUser(id, user)
                .map(eventMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND + id));

    }

    @Transactional(readOnly = true)
    @Override
    public List<EventResponseDTO> getAllEventsForUser(User user) {
        return eventRepository.findAllByUser(user).stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void createEvent(EventRequestDTO dto, User user) {
        Event event = eventMapper.toEntity(dto, user);
        eventRepository.save(event);
    }

    @Transactional
    @Override
    public void updateEvent(Long eventId, EventRequestDTO dto, User user) {
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND + eventId));
        eventMapper.updateEntityFromDto(dto, event);
        eventRepository.save(event);
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId, User user) {
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND + eventId));

        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventResponseDTO> getAllEvents(Pageable pageable) {
        if (!authUserContext.isAdmin()) {
            throw new AdminAccessRequiredException(ADMIN_ACCESS_REQUIRED);
        }
        return eventRepository.findAll(pageable)
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }
}
