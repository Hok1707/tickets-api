package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.*;
import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.entity.Event;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "startDate" ,target = "start")
    @Mapping(source = "endDate" ,target = "end")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    EventDto toDto(Event event);
    List<EventDto> toDtoList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "start" ,target = "startDate")
    @Mapping(source = "end" ,target = "endDate")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    Event fromRequest(CreateEventRequest request);

    @AfterMapping
    default void linkTickets(@MappingTarget Event event) {
        if (event.getTicketTypes() != null) {
            event.getTicketTypes().forEach(ticket -> ticket.setEvent(event));
        }
    }


}

