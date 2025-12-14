package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketValidationRepository extends JpaRepository<TicketValidation, String> {
}