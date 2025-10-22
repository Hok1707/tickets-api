package com.kimhok.tickets.dto.ticket;

import com.kimhok.tickets.common.enums.TicketStatus;
import com.kimhok.tickets.dto.qrCode.QrCodeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TicketDto {
    private String id;
    private TicketStatus status;
    private String userId;
    private String ticketTypeId;
    private List<QrCodeDto> qrCodes;
}
