package com.kimhok.tickets.dto.events;

import com.kimhok.tickets.dto.ticketType.TicketTypeDto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateEventRequest{
    @NotBlank(message = "Event name are required")
    private String name;
    private String description;
    @Future(message = "Event Start must be future date")
    private LocalDateTime start;
    @Future(message = "Event End must be future date")
    private LocalDateTime end;
    @NotBlank(message = "Event venue information are required")
    private String venue;
    @NotBlank(message = "You must put image url")
    private String imageUrl;
    @PositiveOrZero(message = "capacity must be greater than 0")
    private int capacity;
    @NotBlank(message = "Event status must be provided")
    private String status;
    @NotEmpty(message = "At least 1 ticket type must be provided")
    private List<TicketTypeDto> ticketTypes;

}
