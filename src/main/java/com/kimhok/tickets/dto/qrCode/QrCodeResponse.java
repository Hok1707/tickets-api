package com.kimhok.tickets.dto.qrCode;

import com.kimhok.tickets.common.enums.QrCodeStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QrCodeResponse {
    private String id;
    private QrCodeStatus status;
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
