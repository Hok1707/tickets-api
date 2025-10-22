package com.kimhok.tickets.repository;

import com.kimhok.tickets.common.enums.EventStatus;
import com.kimhok.tickets.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    Page<Event> findByOrganizerId(String organizerId, Pageable pageable);
    Page<Event> findByStatus(EventStatus status, Pageable pageable);
    Optional<Event> findByIdAndOrganizerId(String id,String organizerId);
}
