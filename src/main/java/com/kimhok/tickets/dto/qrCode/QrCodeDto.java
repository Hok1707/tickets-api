package com.kimhok.tickets.dto.qrCode;

import com.kimhok.tickets.common.enums.QrCodeStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrCodeDto {
    private String id;
    private QrCodeStatus status;
    private String value;
    private String ticketId;
}
