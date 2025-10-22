package com.kimhok.tickets.dto.payment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {
    private String userId;
    @NotNull(message = "Payment Method is required")
    private String paymentMethod;
    @NotEmpty(message = "You must select at least 1 item")
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private String eventName;
        private String ticketName;
        private int quantity;
        private double price;
    }
}