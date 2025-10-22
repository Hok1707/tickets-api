package com.kimhok.tickets.dto.ticketType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class TicketTypeRequest {
    @NotBlank(message = "Ticket type name is required!")
    private String name;
    private String description;
    @NotNull
    @PositiveOrZero(message = "Price must be greater or equal zero")
    private Double price;
    private Integer totalAvailable;
}
