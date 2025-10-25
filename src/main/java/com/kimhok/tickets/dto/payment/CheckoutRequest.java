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

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Payment Method is required")
    private String paymentMethod;

    @NotEmpty(message = "Items are required")
    private List<CheckoutItemRequest> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CheckoutItemRequest {
        private String eventId;
        private String ticketTypeId;
        private int quantity;
    }
}