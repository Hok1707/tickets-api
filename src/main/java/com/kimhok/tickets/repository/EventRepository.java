package com.kimhok.tickets.repository;

import com.kimhok.tickets.common.enums.EventStatus;
import com.kimhok.tickets.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    @EntityGraph(attributePaths = {"ticketTypes"})
    Page<Event> findAllBy(Pageable pageable);
    Page<Event> findByOrganizerId(String organizerId, Pageable pageable);
    @EntityGraph(attributePaths = {"ticketTypes"})
    Page<Event> findByStatus(EventStatus status, Pageable pageable);
    Optional<Event> findByIdAndOrganizerId(String id,String organizerId);
    @Query("SELECT e FROM Event e JOIN FETCH e.organizer WHERE e.id = :id")
    Optional<Event> findByIdWithOrganizer(@Param("id") String id);

}
