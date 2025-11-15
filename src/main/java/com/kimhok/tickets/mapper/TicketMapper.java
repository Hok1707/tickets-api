package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.ticket.TicketResponse;
import com.kimhok.tickets.dto.ticket.TicketUpdateRequest;
import com.kimhok.tickets.dto.ticket.TicketUserResponse;
import com.kimhok.tickets.entity.Ticket;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TicketTypeMapper.class, QrCodeMapper.class, UserMapper.class, EventMapper.class})
public interface TicketMapper {
    @Mapping(source = "purchaser", target = "purchaser")
    @Mapping(source = "ticketType.id", target = "ticketTypeId")
    @Mapping(source = "ticketType.event.id", target = "eventId")
    @Mapping(source = "ticketType.name",target = "ticketName")
    @Mapping(source = "ticketType.event.name" , target = "eventName")
    TicketResponse toResponse(Ticket entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ticketType", ignore = true),
            @Mapping(target = "purchaser", ignore = true),
            @Mapping(target = "validations", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "QRCode", ignore = true),
            @Mapping(target = "qrCode", ignore = true),
            @Mapping(target = "payment", ignore = true)
    })
    void updateEntityFromRequest(TicketUpdateRequest request, @MappingTarget Ticket entity);

    List<TicketResponse> toListResponse(List<Ticket> tickets);

    @Mapping(source = "ticketType.name", target = "ticketType")
    @Mapping(source = "ticketType.price", target = "price")
    @Mapping(source = "ticketType.event", target = "event")
    @Mapping(source = "purchaser", target = "purchaser")
    @Mapping(source = "qrCode", target = "qrCode")
    TicketUserResponse ticketUserResponse(Ticket ticket);

    List<TicketUserResponse> toTicketUserResponseList(List<Ticket> tickets);
}
