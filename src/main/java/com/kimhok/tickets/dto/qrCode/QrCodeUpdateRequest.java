package com.kimhok.tickets.dto.qrCode;

import com.kimhok.tickets.common.enums.QrCodeStatus;
import lombok.Data;

@Data
public class QrCodeUpdateRequest {
    private QrCodeStatus status;
}
