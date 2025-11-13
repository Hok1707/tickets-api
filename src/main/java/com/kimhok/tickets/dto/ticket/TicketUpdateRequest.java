package com.kimhok.tickets.dto.ticket;

import com.kimhok.tickets.common.enums.TicketStatus;
import com.kimhok.tickets.dto.qrCode.QrCodeUpdateRequest;
import lombok.Data;

@Data
public class TicketUpdateRequest {
    private TicketStatus status;
    private QrCodeUpdateRequest qrCode;
}
