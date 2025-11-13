package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.ticket.TicketRequest;
import com.kimhok.tickets.dto.ticket.TicketResponse;
import com.kimhok.tickets.dto.ticket.TicketUpdateRequest;
import com.kimhok.tickets.dto.ticket.TicketUserResponse;
import com.kimhok.tickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@Slf4j
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    private TicketService ticketService;
    private AuthUtil authUtil;

    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        log.info("========== Create Ticket Controller ==========");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(HttpStatus.CREATED.value(), "Created Ticket Success", ticketService.createTicket(ticketRequest))
        );
    }

    @PutMapping("/status/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@PathVariable String ticketId, @Valid @RequestBody TicketUpdateRequest request) {
        log.info("========== Update Ticket Controller ==========");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK.value(), "Update Ticket Successfully", ticketService.updateTicket(ticketId, request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TicketUserResponse>>> getTicketByUser(@PathVariable String userId) {
        log.info("========== Get Ticket By User {} Controller ==========", userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK.value(), "Get Ticket By User Successfully", ticketService.getUserTicket(userId)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedResponse<TicketResponse>>> getAllTicket(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        log.info("========== Get All Ticket Controller ==========");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK.value(), "Get all tickets", ticketService.getAllTicket(page, size, sortBy, direction)));
    }
}
