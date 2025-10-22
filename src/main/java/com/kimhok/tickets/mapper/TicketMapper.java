package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.ticket.TicketDto;
import com.kimhok.tickets.entity.Ticket;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,uses = {QrCodeMapper.class})
public interface TicketMapper {
    Ticket toEntity(TicketDto ticketDto);

    @Mapping(source = "ticketType.id", target = "ticketTypeId")
    @Mapping(source = "purchaser.id", target = "userId")
    @Mapping(source = "qrCodes", target = "qrCodes")
    TicketDto toDto(Ticket ticket);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ticket partialUpdate(TicketDto ticketDto, @MappingTarget Ticket ticket);
}