package com.kimhok.tickets.dto.events;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EventTicketResponse {
    private String id;
    private String name;
    private String description;
    private String organizerName;
    private String status;
    private String imageUrl;
    private String venue;
    private LocalDateTime start;
    private LocalDateTime end;
}
