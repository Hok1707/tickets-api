package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.dto.UserDTO;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        log.info("User Controller Get User Profile..." );
        User user = AuthUtil.getCurrentUser();
        if (user == null) {
            throw new AuthenticationException("User is not authenticated"){};
        }
        Map<String,Object> profile = new HashMap<>();
        profile.put("username",user.getUsername());
        profile.put("email",user.getEmail());
        profile.put("phoneNumber",user.getPhoneNumber());
        profile.put("role",user.getRoleName());
        profile.put("status",user.getStatus());
        profile.put("createdAt",user.getCreatedAt());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),"Get User Profile Success",profile));
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
}
