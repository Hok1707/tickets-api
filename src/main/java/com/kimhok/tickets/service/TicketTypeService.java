package com.kimhok.tickets.service;

import com.google.zxing.WriterException;
import com.kimhok.tickets.dto.ticket.TicketDto;

import java.io.IOException;

public interface TicketTypeService {
    TicketDto purchasedTicket(String userId, String ticketTypeId) throws IOException, WriterException;
}
