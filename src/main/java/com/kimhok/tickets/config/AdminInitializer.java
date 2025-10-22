package com.kimhok.tickets.config;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.entity.Role;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.entity.UserRole;
import com.kimhok.tickets.repository.RoleRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String ADMIN_EMAIL = "hengkimhok07@gmail.com";
    private static final String ADMIN_PASSWORD = "admin@007";

    @Override
    public void run(String... args) {
        createAdminRoleIfNotExist();
        createAdminUser();
    }

    private void createAdminRoleIfNotExist() {
        roleRepository.findRoleByName(ADMIN_ROLE).orElseGet(() -> {
            Role role = new Role();
            role.setName(ADMIN_ROLE);
            Role savedRole = roleRepository.save(role);
            System.out.println("✅ Created role: " + savedRole.getName());
            return savedRole;
        });
    }

    private void createAdminUser() {
        User admin = userRepository.findByEmail(ADMIN_EMAIL).orElseGet(() -> {
            User user = new User();
            user.setEmail(ADMIN_EMAIL);
            user.setUsername("Admin");
            user.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            System.out.println("✅ Created admin user: " + ADMIN_EMAIL);
            return savedUser;
        });

        Role adminRole = roleRepository.findRoleByName(ADMIN_ROLE)
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        if (!userRoleRepository.existsByUserAndRole(admin, adminRole)) {
            UserRole userRole = new UserRole();
            userRole.setUser(admin);
            userRole.setRole(adminRole);
            userRoleRepository.save(userRole);
            System.out.println("✅ Assigned ADMIN role to: " + ADMIN_EMAIL);
        } else {
            System.out.println("ℹ️ Admin already has ADMIN role: " + ADMIN_EMAIL);
        }
    }
}