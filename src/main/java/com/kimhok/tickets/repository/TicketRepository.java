package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByPurchaser_Id(String userId);

    int countByTicketTypeId(String ticketTypeId);
}