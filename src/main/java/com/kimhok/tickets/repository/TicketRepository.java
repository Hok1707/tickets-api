package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByPurchaser_Id(String userId);

    @EntityGraph(attributePaths = {"purchaser", "ticketType", "ticketType.event"})
    Page<Ticket> findAllBy(Pageable pageable);

    int countByTicketTypeId(String ticketTypeId);
    @Query("SELECT t from Ticket t JOIN t.qrCode q WHERE q.value = :value")
    Optional<Ticket> findByQrValue(@Param("value") String value);
}