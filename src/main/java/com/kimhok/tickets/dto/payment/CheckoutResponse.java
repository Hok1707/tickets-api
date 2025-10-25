package com.kimhok.tickets.dto.payment;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private String orderId;
    private String billNumber;
    private String userId;
    private String paymentMethod;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal transactionFee;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private String eventName;
        private String ticketName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}