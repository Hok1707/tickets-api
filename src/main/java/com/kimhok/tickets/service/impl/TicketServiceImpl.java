package com.kimhok.tickets.service.impl;

import com.google.zxing.WriterException;
import com.kimhok.tickets.common.enums.QrCodeStatus;
import com.kimhok.tickets.common.enums.TicketMethod;
import com.kimhok.tickets.common.enums.TicketStatus;
import com.kimhok.tickets.common.enums.TicketValidationStatus;
import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.common.utils.QrCodeGenerator;
import com.kimhok.tickets.dto.qrCode.QrValueRequest;
import com.kimhok.tickets.dto.ticket.TicketRequest;
import com.kimhok.tickets.dto.ticket.TicketResponse;
import com.kimhok.tickets.dto.ticket.TicketUpdateRequest;
import com.kimhok.tickets.dto.ticket.TicketUserResponse;
import com.kimhok.tickets.entity.QrCode;
import com.kimhok.tickets.entity.Ticket;
import com.kimhok.tickets.entity.TicketType;
import com.kimhok.tickets.entity.TicketValidation;
import com.kimhok.tickets.exception.QrGenerationException;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.exception.TicketSoldOutException;
import com.kimhok.tickets.mapper.QrCodeMapper;
import com.kimhok.tickets.mapper.TicketMapper;
import com.kimhok.tickets.repository.*;
import com.kimhok.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private QrCodeMapper qrCodeMapper;
    private final TicketValidationRepository ticketValidationRepository;

    @Transactional
    @Override
    public List<TicketResponse> createTicket(TicketRequest request) {
        log.info("======== Create Ticket Service =========");

        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        var purchaser = userRepository.findById(request.getPurchaserId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchaser not found"));

        List<TicketResponse> allTickets = new ArrayList<>();

        for (var item : order.getItems()) {
            var ticketTypeId = item.getTicketType();

            var ticketType = ticketTypeRepository.findById(ticketTypeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket Type not found for ID: " + ticketTypeId));

            int soldCount = ticketRepository.countByTicketTypeId(ticketTypeId);
            int remaining = ticketType.getTotalAvailable() - soldCount;

            if (remaining <= 0) {
                throw new TicketSoldOutException("Tickets for type " + ticketType.getName() + " are sold out.");
            }

            int quantity = item.getQuantity();
            if (quantity > remaining) {
                throw new TicketSoldOutException("Only " + remaining + " tickets available for " + ticketType.getName());
            }
            var ticketValidations = new ArrayList<TicketValidation>();
            var tickets = new ArrayList<Ticket>();
            for (int i = 0; i < quantity; i++) {
                Ticket ticket = new Ticket();
                ticket.setStatus(TicketStatus.PURCHASED);
                ticket.setTicketType(ticketType);
                ticket.setPurchaser(purchaser);

                TicketValidation ticketValidation = new TicketValidation();
                ticketValidation.setTicket(ticket);
                ticketValidation.setStatus(TicketValidationStatus.VALID);

                String qrValue = order.getId() + "-" + UUID.randomUUID();
                try {
                    String qrBase64 = QrCodeGenerator.generateQRCodeBase64(qrValue, 250, 250);
                    QrCode qrCode = QrCode.builder()
                            .id(UUID.randomUUID().toString())
                            .status(QrCodeStatus.ACTIVE)
                            .value(qrBase64)
                            .ticket(ticket)
                            .build();
                    ticket.setQRCode(qrCode);
                } catch (WriterException | IOException e) {
                    throw new QrGenerationException("Failed to generate QR Code", e.getMessage());
                }

                tickets.add(ticket);
                ticketValidations.add(ticketValidation);
            }
            List<TicketValidation> savedTicketValidation = ticketValidationRepository.saveAll(ticketValidations);

            List<Ticket> savedTickets = ticketRepository.saveAll(tickets);
            ticketType.setTotalAvailable(ticketType.getTotalAvailable() - quantity);
            ticketTypeRepository.save(ticketType);
            allTickets.addAll(ticketMapper.toListResponse(savedTickets));

            log.info("===== Successfully created {} ticket(s) for purchaser {} =====",
                    quantity, purchaser.getId());
        }

        return allTickets;
    }

    @Override
    public TicketResponse updateTicket(String ticketId, TicketUpdateRequest request) {
        log.info("======= Update Ticket in Ticket Service {} ======= ", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket Not Found!"));
        ticketMapper.updateEntityFromRequest(request, ticket);
        if (request.getQrCode() != null && ticket.getQrCode() != null) {
            qrCodeMapper.updateEntityFromRequest(request.getQrCode(), ticket.getQrCode());
        }
        Ticket ticketUpdated = ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticketUpdated);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketUserResponse> getUserTicket(String userId) {
        log.info("======= Get Ticket By user {} Ticket Service======= ", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        var tickets = ticketRepository.findByPurchaser_Id(userId);
        return ticketMapper.toTicketUserResponseList(tickets);
    }

    @Override
    public PagedResponse<TicketResponse> getAllTicket(int page, int size, String sortBy, String direction) {
        log.info("======= Get all tickets page {} & size {} ======= ", page, size);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> ticketPage = ticketRepository.findAllBy(pageable);
        List<TicketResponse> items = ticketMapper.toListResponse(ticketPage.getContent());
        return new PagedResponse<>(
                items,
                ticketPage.getNumber(),
                ticketPage.getTotalPages(),
                ticketPage.getTotalElements(),
                ticketPage.getSize()
        );
    }

}
