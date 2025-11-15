package com.kimhok.tickets.dto.ticket;

import com.kimhok.tickets.common.enums.TicketStatus;
import com.kimhok.tickets.dto.auth.PurchaserResponse;
import com.kimhok.tickets.dto.qrCode.QrCodeResponse;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private String id;
    private TicketStatus status;
    private String ticketTypeId;
    private String eventId;
    private String ticketName;
    private String eventName;
    private PurchaserResponse purchaser;
    private QrCodeResponse qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}