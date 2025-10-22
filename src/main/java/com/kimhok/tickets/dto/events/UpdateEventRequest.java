package com.kimhok.tickets.dto.events;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateEventRequest {
    private String name;
    private String description;
    private String imageUrl;
    private int capacity;
    private String status;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private List<UpdateTicketTypeRequest> ticketTypes;
}