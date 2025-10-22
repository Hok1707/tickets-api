package com.kimhok.tickets.controller;

import com.google.zxing.WriterException;
import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.dto.ticket.TicketDto;
import com.kimhok.tickets.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types/")
@RequiredArgsConstructor
@Slf4j
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;
    private final AuthUtil authUtil;

    /**
     * Purchase a ticket type from a given event
     * Example: POST /api/v1/events/{eventId}/ticket-types/{ticketTypeId}/purchase
     */
    @PostMapping("{ticketTypeId}/purchase")
    public ResponseEntity<Void> purchasedTicket(
            @PathVariable String eventId,
            @PathVariable String ticketTypeId
    ) throws IOException, WriterException {
        String userId = authUtil.getCurrentUserId();
        log.info("User {} is purchasing ticketType {} for event {}", userId, ticketTypeId, eventId);
        ticketTypeService.purchasedTicket(userId, ticketTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}