package com.kimhok.tickets.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BakongTransactionResponse {
    private String responseMessage;
    private Integer responseCode;
    private TransactionData data;
    private String status;
    private Integer errorCode;

    @Data
    public static class TransactionData {
        private String hash;
        private String fromAccountId;
        private String toAccountId;
        private Double amount;
        private String currency;
        private Double createdDateMs;
        private Double acknowledgedDateMs;
    }
}

