package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.entity.Role;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.entity.UserRole;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.repository.RoleRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.repository.UserRoleRepository;
import com.kimhok.tickets.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void assignRoleToUser(String userEmail, String roleName, Optional<User> assignedBy) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("This email not found: " + userEmail));

        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        boolean hasRole = user.hasRole(roleName);

        if (hasRole) {
            throw new ResourceNotFoundException("User already has role: " + roleName);
        }

        UserRole userRole = new UserRole(user, role, assignedBy.orElse(null));
        UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("User Role has been assigned '{}'", savedUserRole);
        String assignedByInfo = assignedBy.map(User::getEmail).orElse("System");
        log.info("Assigned role '{}' to user '{}' by user '{}'", roleName, userEmail, assignedByInfo);
    }

    @Transactional
    @Override
    public void updateUserRole(String userId, String newRoleName, User assignedBy) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role newRole = roleRepository.findRoleByName(newRoleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + newRoleName));

        if (user.hasRole(newRoleName)) {
            throw new IllegalArgumentException("User already has role: " + newRoleName);
        }

        UserRole userRole = user.getUserRole();
        if (userRole == null) {
            userRole = new UserRole(user, newRole, assignedBy);
            user.setUserRole(userRole);
        } else {
            String oldRoleName = userRole.getRole().getName();
            userRole.setRole(newRole);
            userRole.setAssignedBy(assignedBy);
            log.info("Updated user '{}' role from '{}' to '{}' by '{}'",
                    userId, oldRoleName, newRoleName, assignedBy.getEmail());
        }

        userRoleRepository.save(userRole);
        log.info("Role updated successfully for user: {}", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    @Override
    public String getUserRole(String email) {
        User user = findByEmail(email);
        return user.getRoleName();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByRole(String roleName) {
        return userRepository.countByRoleName(roleName);
    }
}
