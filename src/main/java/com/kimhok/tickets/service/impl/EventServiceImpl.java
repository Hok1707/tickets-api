package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.EventDto;
import com.kimhok.tickets.dto.events.CreateEventRequest;
import com.kimhok.tickets.entity.Event;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    @Override
    @Transactional
    public EventDto createEvent(CreateEventRequest request) {
        log.info("Service event starting to create...");

        String organizerId = AuthUtil.getCurrentUserId();
        if (organizerId == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Organizer not found"));

        Event event = eventMapper.fromRequest(request);

        event.setOrganizer(organizer);

        Event saved = eventRepository.save(event);

        return eventMapper.toDto(saved);
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
    public EventDto getEventById(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new ResourceNotFoundException("Event Not found "+eventId));
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public void deleteEventById(String eventId) {
        Event isExisting = eventRepository.findById(eventId)
                .orElseThrow(()-> new ResourceNotFoundException("Event Not found "+eventId));
        eventRepository.delete(isExisting);
    }
}
