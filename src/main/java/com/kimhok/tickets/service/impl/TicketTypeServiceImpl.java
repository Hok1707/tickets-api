package com.kimhok.tickets.service.impl;

import com.google.zxing.WriterException;
import com.kimhok.tickets.common.enums.TicketStatus;
import com.kimhok.tickets.dto.qrCode.QrCodeDto;
import com.kimhok.tickets.dto.ticket.TicketDto;
import com.kimhok.tickets.entity.QrCode;
import com.kimhok.tickets.entity.Ticket;
import com.kimhok.tickets.entity.TicketType;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.exception.TicketSoldOutException;
import com.kimhok.tickets.mapper.TicketMapper;
import com.kimhok.tickets.repository.TicketRepository;
import com.kimhok.tickets.repository.TicketTypeRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.service.QrCodeService;
import com.kimhok.tickets.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    @Override
    public TicketDto purchasedTicket(String userId, String ticketTypeId) throws IOException, WriterException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found: " + ticketTypeId));

        int purchasedCount = ticketTypeRepository.countTicketTypeById(ticketTypeId);
        if (purchasedCount >= ticketType.getTotalAvailable()) {
            throw new TicketSoldOutException(ticketTypeId);
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setPurchaser(user);
        ticket.setTicketType(ticketType);

        Ticket savedTicket = ticketRepository.save(ticket);

        QrCodeDto qrCodeDto = qrCodeService.generateQr(savedTicket);

        QrCode qrEntity = QrCode.builder()
                .id(qrCodeDto.getId())
                .value(qrCodeDto.getValue())
                .status(qrCodeDto.getStatus())
                .ticket(savedTicket)
                .build();

        savedTicket.setQrCodes(Collections.singletonList(qrEntity));

        Ticket finalSaved = ticketRepository.save(savedTicket);

        log.info("Ticket purchased successfully: Ticket ID={}, QR={}", finalSaved.getId(), qrCodeDto.getId());

        return ticketMapper.toDto(finalSaved);
    }
}