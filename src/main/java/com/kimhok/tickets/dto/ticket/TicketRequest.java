package com.kimhok.tickets.dto.ticket;

import lombok.Data;

@Data
public class TicketRequest {
    private String orderId;
    private String purchaserId;
}
