package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.config.security.CustomUserDetails;
import com.kimhok.tickets.dto.role.RoleAssignedRequest;
import com.kimhok.tickets.dto.role.RoleUpdateRequest;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(
            @RequestBody RoleAssignedRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails adminDetail = (CustomUserDetails) authentication.getPrincipal();
        User admin = adminDetail.getUser();

        userRoleService.assignRoleToUser(request.getUserEmail(), request.getRoleName(), Optional.ofNullable(admin));
        return ResponseEntity.ok(Map.of("messages", "Role assigned successfully"));
    }

    @PostMapping("/update-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateUserRole(@Valid @RequestBody RoleUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails adminDetails = (CustomUserDetails) authentication.getPrincipal();
        User admin = adminDetails.getUser();

        userRoleService.updateUserRole(request.getUserEmail(), request.getNewRoleName(), admin);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Role Update Successfully",
                Map.of("New Role", request.getNewRoleName(),
                "User Email", request.getUserEmail()))
        );
    }

    @GetMapping("/role/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserRole(@PathVariable String email) {
        try {
            String roleName = userRoleService.getUserRole(email);
            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "role", roleName != null ? roleName : "No role assigned"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/by-role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String roleName) {
        var users = userRoleService.getUsersByRole(roleName);
        long count = userRoleService.countUsersByRole(roleName);

        return ResponseEntity.ok(Map.of(
                "roleName", roleName,
                "userCount", count,
                "users", users.stream().map(user -> Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "username", user.getUsername()
                )).toList()
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "status", user.getStatus(),
                "role", user.getRoleName() != null ? user.getRoleName() : "No role assigned"
        ));
    }

    @GetMapping("/debug-role/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> debugUserRole(@PathVariable String email) {
        try {
            String debug = userRoleService.debugUserRole(email);
            return ResponseEntity.ok(Map.of("debug", debug));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
