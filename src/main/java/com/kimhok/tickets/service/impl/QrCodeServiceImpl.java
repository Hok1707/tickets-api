package com.kimhok.tickets.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kimhok.tickets.common.enums.QrCodeStatus;
import com.kimhok.tickets.dto.qrCode.QrCodeDto;
import com.kimhok.tickets.entity.QrCode;
import com.kimhok.tickets.entity.Ticket;
import com.kimhok.tickets.mapper.QrCodeMapper;
import com.kimhok.tickets.repository.QrCodeRepository;
import com.kimhok.tickets.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrRepository;
    private final QRCodeWriter qrCodeWriter;
    private final QrCodeMapper qrMapper;

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    @Override
    public QrCodeDto generateQr(Ticket ticket) throws IOException, WriterException {
        String uniqueId = UUID.randomUUID().toString();
        String qrCodeImage = generateQrCodeImage(uniqueId);

        QrCode qrCode = QrCode.builder()
                .id(uniqueId)
                .status(QrCodeStatus.ACTIVE)
                .value(qrCodeImage)
                .ticket(ticket)
                .build();

        QrCode saved = qrRepository.saveAndFlush(qrCode);
        log.info("Generated QR Code for ticket: {}", ticket.getId());

        return qrMapper.toDto(saved);
    }

    private String generateQrCodeImage(String uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(uniqueId, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
}