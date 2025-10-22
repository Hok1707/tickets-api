package com.kimhok.tickets.exception;

public class TicketSoldOutException extends RuntimeException {
    public TicketSoldOutException(String eventId) {
        super("Tickets for event " + eventId + " are sold out.");
    }
}
