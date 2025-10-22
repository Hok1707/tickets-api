package com.kimhok.tickets.dto.ticketType;

import lombok.Data;

@Data
public class TicketTypeDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer totalAvailable;
}
