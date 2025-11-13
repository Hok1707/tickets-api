package com.kimhok.tickets.dto.ticket;

import com.kimhok.tickets.dto.auth.PurchaserResponse;
import com.kimhok.tickets.dto.events.EventTicketResponse;
import com.kimhok.tickets.dto.qrCode.QrCodeResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketUserResponse {
    private String id;
    private String ticketType;
    private Double price;
    private PurchaserResponse purchaser;
    private EventTicketResponse event;
    private QrCodeResponse qrCode;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
