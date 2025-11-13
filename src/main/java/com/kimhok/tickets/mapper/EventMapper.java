package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.dto.events.EventDto;
import com.kimhok.tickets.dto.events.EventTicketResponse;
import com.kimhok.tickets.dto.events.UpdateEventRequest;
import com.kimhok.tickets.entity.Event;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TicketTypeMapper.class, UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    Event fromRequest(CreateEventRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    @Mapping(target = "ticketTypes", ignore = true)
    void fromUpdateRequest(UpdateEventRequest updateEventRequest, @MappingTarget Event event);

    @Mapping(source = "organizer.username", target = "organizerName")
    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    EventTicketResponse toResponse(Event event);

    @AfterMapping
    default void linkTickets(@MappingTarget Event event) {
        if (event.getTicketTypes() != null) {
            event.getTicketTypes().forEach(ticket -> ticket.setEvent(event));
        }
    }
}

