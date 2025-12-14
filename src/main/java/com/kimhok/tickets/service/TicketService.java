package com.kimhok.tickets.service;

import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.qrCode.QrValueRequest;
import com.kimhok.tickets.dto.ticket.TicketRequest;
import com.kimhok.tickets.dto.ticket.TicketResponse;
import com.kimhok.tickets.dto.ticket.TicketUpdateRequest;
import com.kimhok.tickets.dto.ticket.TicketUserResponse;

import java.util.List;

public interface TicketService {
    List<TicketResponse> createTicket(TicketRequest request);
    TicketResponse updateTicket(String ticketId, TicketUpdateRequest request);
    List<TicketUserResponse> getUserTicket(String userId);
    PagedResponse<TicketResponse> getAllTicket(int page, int size, String sortBy, String direction);
}
