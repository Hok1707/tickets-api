package com.kimhok.tickets.repository;

import com.kimhok.tickets.entity.Role;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    boolean existsByUserAndRole(User admin, Role adminRole);
}
