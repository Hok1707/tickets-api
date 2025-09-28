package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.dto.auth.*;
import com.kimhok.tickets.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Auth Controller register Processing...");
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Register Successfully",response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Auth Controller login Processing...");
        LoginResponseDTO loginResponseDTO = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),"Login Successfully",loginResponseDTO));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Auth Controller refreshToken Processing...");
        RefreshTokenResponseDTO response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid
            @RequestBody ChangePasswordRequestDTO request
    ) {
        log.info("Auth Controller changePassword Processing...");
        authService.changePassword(
                request.getCurrentPassword(),
                request.getNewPassword(),
                request.getConfirmPassword()
        );

        return ResponseEntity.ok("Change Password Successfully");
    }
}