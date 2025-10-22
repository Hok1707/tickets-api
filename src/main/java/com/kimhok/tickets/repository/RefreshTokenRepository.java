package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.RefreshToken;
import com.kimhok.tickets.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}