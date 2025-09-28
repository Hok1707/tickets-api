package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.auth.*;

import java.util.UUID;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
    RefreshTokenResponseDTO refreshToken(String refreshToken);
    void changePassword(String currentPassword, String newPassword, String confirmPassword);

}
