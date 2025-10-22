package com.kimhok.tickets.dto.payment;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private String orderId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
}