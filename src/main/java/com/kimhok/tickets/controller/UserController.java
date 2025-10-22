package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.dto.auth.StatusUpdateRequest;
import com.kimhok.tickets.dto.auth.UserDTO;
import com.kimhok.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        log.info("User Controller Get User Profile...");
        Map<String,Object> profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Get User Profile Success", profile));
    }

    @GetMapping("/list-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listAllUsers(){
        log.info("User Controller List All User...");
        List<UserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Get All Users",allUsers));
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @Valid @PathVariable String userId,
            @RequestBody UserDTO userDTO
    ) {
        log.info("User Controller Update User..." + userDTO.getEmail());
        UserDTO updatedUser = userService.updateProfile(userDTO, userId);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "Update User Successfully", updatedUser)
        );
    }

    @GetMapping("/active-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> listActiveUser(){
        log.info("User Controller List Active User...");
        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List All Active User")
                .data(userService.getActiveUser())
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeUser(@PathVariable String id) {
        log.info("User Controller Remove User '{}'", id);
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-status/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
            @PathVariable String userId,
            @RequestBody StatusUpdateRequest request
    ) {
        UserDTO updatedUser = userService.updateStatus(userId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "User status updated successfully",
                updatedUser
        ));
    }
}
