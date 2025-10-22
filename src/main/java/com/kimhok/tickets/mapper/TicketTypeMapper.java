package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.ticketType.TicketTypeDto;
import com.kimhok.tickets.dto.ticketType.TicketTypeRequest;
import com.kimhok.tickets.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketTypeMapper {
    @Mapping(target = "id", ignore = true)
    TicketType fromRequest(TicketTypeRequest request);

    TicketTypeDto toDto(TicketType ticketType);
}
