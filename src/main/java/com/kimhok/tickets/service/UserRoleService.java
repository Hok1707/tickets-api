package com.kimhok.tickets.service;

import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    void assignRoleToUser(String userEmail, String roleName, Optional<User> assignedBy);
    void updateUserRole(String userEmail, String newRoleName, User assignedBy);
    User findByEmail(String email);
    String getUserRole(String email);
    List<User> getUsersByRole(String roleName);
    long countUsersByRole(String roleName);
    boolean validateUserHasSingleRole(String email);
    UserRole getUserRoleInfo(String email);
    String debugUserRole(String email);

}
