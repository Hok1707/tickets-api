package com.kimhok.tickets.dto.payment;

import lombok.Data;

@Data
public class BakongTransactionResponse {
    private String responseMessage;
    private int responseCode;
    private TransactionData data;

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

