package com.kimhok.tickets.dto.events;

import com.kimhok.tickets.common.enums.EventStatus;
import com.kimhok.tickets.dto.ticketType.TicketTypeDto;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDto {
    private String id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private String imageUrl;
    private int capacity;
    private EventStatus status;
    private List<TicketTypeDto> ticketTypes;

}
