package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.TicketTypeDto;
import com.kimhok.tickets.entity.TicketType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    TicketType toEntity(TicketTypeDto dto);
    TicketTypeDto toDto(TicketType ticketType);
    List<TicketTypeDto> toDtoListTickets(List<TicketType> tickets);

}

