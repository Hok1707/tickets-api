package com.kimhok.tickets.service;

import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.events.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.dto.events.UpdateEventRequest;

import java.util.Optional;

public interface EventService {
    EventDto createEvent(CreateEventRequest request);
    Optional<EventDto> getEventForOrganizer(String id);
    PagedResponse<EventDto> getAllEvent(int page, int size, String sortBy, String direction);
    PagedResponse<EventDto> getAllEventByOrganizer(int page,int size,String sortBy,String direction);
    EventDto getEventById(String eventId);
    void deleteEventById(String eventId);
    EventDto updateEvent(String eventId, UpdateEventRequest request);
    PagedResponse<EventDto> getPublishedEvents(String status,int page, int size, String sortBy, String direction);

}
