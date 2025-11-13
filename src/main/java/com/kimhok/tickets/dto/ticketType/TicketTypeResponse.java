package com.kimhok.tickets.dto.ticketType;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketTypeResponse {
    private String id;
    private String eventId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
