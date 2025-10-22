package com.kimhok.tickets.service;

import com.google.zxing.WriterException;
import com.kimhok.tickets.dto.qrCode.QrCodeDto;
import com.kimhok.tickets.entity.Ticket;

import java.io.IOException;

public interface QrCodeService {
    QrCodeDto generateQr(Ticket ticket) throws WriterException, IOException;
}
