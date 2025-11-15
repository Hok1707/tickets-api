package com.kimhok.tickets.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDashboardResponse {
    private BigDecimal netProfit;
    private BigDecimal transactionFee;
    private BigDecimal totalIncome;
}
