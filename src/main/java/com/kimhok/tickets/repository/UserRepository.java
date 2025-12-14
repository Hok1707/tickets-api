package com.kimhok.tickets.repository;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRole ur LEFT JOIN FETCH ur.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN u.userRole ur JOIN ur.role r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT COUNT(u) FROM User u JOIN u.userRole ur JOIN ur.role r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);

    List<User> findByStatus(UserStatus status);

    Optional<User> findByResetToken(String resetToken);
    Optional<User> findByEmailVerifyToken(String emailVerifyToken);

}
