package com.kimhok.tickets.service;

import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;

public interface EventService {
    EventDto createEvent(CreateEventRequest request);
    PagedResponse<EventDto> getAllEvent(int page, int size, String sortBy, String direction);
    EventDto getEventById(String eventId);
    void deleteEventById(String eventId);
}
