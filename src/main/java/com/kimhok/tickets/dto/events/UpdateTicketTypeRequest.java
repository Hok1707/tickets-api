package com.kimhok.tickets.dto.events;

import lombok.Data;

@Data
public class UpdateTicketTypeRequest {
    private String id;
    private String name;
    private double price;
    private Integer totalAvailable;
    private String description;
}
