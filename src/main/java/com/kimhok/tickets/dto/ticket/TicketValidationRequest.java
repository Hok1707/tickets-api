package com.kimhok.tickets.dto.ticket;

import com.kimhok.tickets.entity.Ticket;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketValidationRequest {
    private String validationStatus;
    private String validationMethod;
    private Ticket ticket;
}
