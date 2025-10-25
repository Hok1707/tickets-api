package com.kimhok.tickets.dto.orderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    @NotNull(message = "Event ID is required")
    private String eventId;
    @NotNull(message = "Ticket Type ID is required")
    private String ticketTypeId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @NotNull(message = "Price is required")
    private double price;
    private String eventName;
    private String ticketName;
}