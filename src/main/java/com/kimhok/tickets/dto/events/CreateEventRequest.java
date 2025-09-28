package com.kimhok.tickets.dto.events;

import com.kimhok.tickets.dto.TicketTypeDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CreateEventRequest{
    @NotBlank(message = "Event name are required")
    private String name;
    @Future(message = "Event Start must be future date")
    private LocalDateTime start;
    @Future(message = "Event End must be future date")
    private LocalDateTime end;
    @NotBlank(message = "Event venue information are required")
    private String venue;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    @NotBlank(message = "Event status must be provided")
    private String status;
    @NotEmpty(message = "At least 1 ticket type must be provided")
    private List<TicketTypeDto> ticketTypes;

}
