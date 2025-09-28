package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventDto createEvent(CreateEventRequest request);
    List<EventDto> getAllEvent();
    EventDto getEventById(String eventId);
    void deleteEventById(String eventId);
}
