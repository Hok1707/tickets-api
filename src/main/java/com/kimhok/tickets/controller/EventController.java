package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<EventDto>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        log.info("Create Event Controller");
        EventDto eventDto = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Event Created Successfully", eventDto));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<EventDto>> listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(eventService.getAllEvent(page, size, sortBy, direction));
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventDto>> getEventById(@PathVariable String eventId){
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Get Event By Id", eventService.getEventById(eventId)));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable String eventId) {
        eventService.deleteEventById(eventId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Event deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}