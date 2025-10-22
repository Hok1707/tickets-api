package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.enums.EventStatus;
import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.events.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.dto.events.UpdateEventRequest;
import com.kimhok.tickets.dto.events.UpdateTicketTypeRequest;
import com.kimhok.tickets.entity.Event;
import com.kimhok.tickets.entity.TicketType;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.mapper.EventMapper;
import com.kimhok.tickets.repository.EventRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public EventDto createEvent(CreateEventRequest request) {
        log.info("Creating new event...");
        String organizerId = authUtil.getCurrentUserId();
        if (organizerId == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Organizer not found"));

        Event event = eventMapper.fromRequest(request);
        event.setOrganizer(organizer);

        Event saved = eventRepository.save(event);

        log.info("Event created successfully with {} ticket types",
                saved.getTicketTypes() != null ? saved.getTicketTypes().size() : 0);

        return eventMapper.toDto(saved);
    }

    @Override
    public Optional<EventDto> getEventForOrganizer(String id) {
        String organizerId = authUtil.getCurrentUserId();
        userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer Not Found"));

        Event event = eventRepository.findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found for this organizer"));

        return Optional.of(eventMapper.toDto(event));
    }

    @Override
    public PagedResponse<EventDto> getAllEvent(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Event> eventsPage = eventRepository.findAll(pageable);

        List<EventDto> items = eventsPage.map(eventMapper::toDto).getContent();

        return new PagedResponse<>(
                items,
                eventsPage.getNumber(),
                eventsPage.getTotalPages(),
                eventsPage.getTotalElements(),
                eventsPage.getSize()
        );
    }

    @Override
    public PagedResponse<EventDto> getAllEventByOrganizer(int page, int size, String sortBy, String direction) {
        String organizerId = authUtil.getCurrentUserId();
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Event> eventPage = eventRepository.findByOrganizerId(organizerId,pageable);
        List<EventDto> items = eventMapper.toDtoList(eventPage.getContent());
        return new PagedResponse<>(
                items,
                eventPage.getNumber(),
                eventPage.getTotalPages(),
                eventPage.getTotalElements(),
                eventPage.getSize()
        );
    }

    @Override
    public EventDto getEventById(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not found " + eventId));
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public void deleteEventById(String eventId) {
        Event isExisting = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not found " + eventId));
        eventRepository.delete(isExisting);
    }

    @Transactional
    @Override
    public EventDto updateEvent(String eventId, UpdateEventRequest request) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        eventMapper.fromUpdateRequest(request, existingEvent);

        if (request.getTicketTypes() != null) {
            existingEvent.getTicketTypes().removeIf(existing ->
                    request.getTicketTypes().stream().noneMatch(
                            r -> r.getId() != null && r.getId().equals(existing.getId()))
            );

            for (UpdateTicketTypeRequest tReq : request.getTicketTypes()) {
                if (tReq.getId() != null) {
                    TicketType existingTicket = existingEvent.getTicketTypes().stream()
                            .filter(t -> t.getId().equals(tReq.getId()))
                            .findFirst()
                            .orElse(null);
                    if (existingTicket != null) {
                        existingTicket.setName(tReq.getName());
                        existingTicket.setPrice(tReq.getPrice());
                        existingTicket.setTotalAvailable(tReq.getTotalAvailable());
                        existingTicket.setDescription(tReq.getDescription());
                    }
                } else {
                    TicketType newTicket = new TicketType();
                    newTicket.setName(tReq.getName());
                    newTicket.setPrice(tReq.getPrice());
                    newTicket.setTotalAvailable(tReq.getTotalAvailable());
                    newTicket.setEvent(existingEvent);
                    newTicket.setDescription(tReq.getDescription());
                    existingEvent.getTicketTypes().add(newTicket);
                }
            }
        }

        Event saved = eventRepository.save(existingEvent);
        return eventMapper.toDto(saved);
    }

    @Override
    public PagedResponse<EventDto> getPublishedEvents(String status, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Event> eventPage = eventRepository.findByStatus(EventStatus.PUBLISHED,pageable);
        List<EventDto> items = eventMapper.toDtoList(eventPage.getContent());
        return new PagedResponse<>(
                items,
                eventPage.getNumber(),
                eventPage.getTotalPages(),
                eventPage.getTotalElements(),
                eventPage.getSize()
        );
    }

}
