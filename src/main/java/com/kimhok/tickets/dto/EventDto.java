package com.kimhok.tickets.dto;

import com.kimhok.tickets.common.enums.EventStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventDto {
    private String id;
    @NotBlank(message = "Event name is required!")
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank(message = "Venue information is required")
    private String venue;
    @Future(message = "Sale start date must be future date")
    private LocalDateTime saleStart;
    @Future(message = "Sale end date must be future date")
    private LocalDateTime saleEnd;
    @NotNull(message = "Event status must be provided")
    private EventStatus status;
    @NotEmpty(message = "At least 1 ticket type is required")
    private List<TicketTypeDto> ticketTypes;

}
