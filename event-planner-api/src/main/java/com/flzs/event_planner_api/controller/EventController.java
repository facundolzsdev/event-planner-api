package com.flzs.event_planner_api.controller;

import com.flzs.event_planner_api.model.dto.event.*;
import com.flzs.event_planner_api.model.entity.User;
import com.flzs.event_planner_api.service.*;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Get all events for the current user",
            description = "Returns a list of all events associated with the currently authenticated user."
    )
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<EventResponseDTO> events = eventService.getAllEventsForUser(currentUser);
        return ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Get a specific event by ID",
            description = "Returns the details of a specific event, if it belongs to the current user."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        EventResponseDTO event = eventService.getEventByIdForUser(id, currentUser);
        return ResponseEntity.ok(event);
    }

    @Operation(
            summary = "Create a new event",
            description = "Creates a new event for the currently authenticated user."
    )
    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventRequestDTO dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.createEvent(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Update an existing event",
            description = "Updates the specified event if it belongs to the current user."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.updateEvent(id, dto, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete an event",
            description = "Deletes the specified event if it belongs to the current user."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        eventService.deleteEvent(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all events (admin only)",
            description = "Returns a paginated list of all events in the system. This endpoint is intended for administrators."
    )
    @GetMapping("/admin")
    public ResponseEntity<List<EventResponseDTO>> getAllEventsForAdmin(
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }
}
