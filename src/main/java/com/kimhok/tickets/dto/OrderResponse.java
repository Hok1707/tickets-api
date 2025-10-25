package com.kimhok.tickets.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String status;
    private String paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal transactionFee;
    private BigDecimal totalAmount;
    private String billNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
