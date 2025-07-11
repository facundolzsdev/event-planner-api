package com.flzs.event_planner_api.controller;

import com.flzs.event_planner_api.model.dto.event.*;
import com.flzs.event_planner_api.model.entity.User;
import com.flzs.event_planner_api.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";

    private final UserService userService;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<EventResponseDTO> events = eventService.getAllEventsForUser(currentUser);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        EventResponseDTO event = eventService.getEventByIdForUser(id, currentUser);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventRequestDTO dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.createEvent(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.updateEvent(id, dto, currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.deleteEvent(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<List<EventResponseDTO>> getAllEventsForAdmin(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }
}
