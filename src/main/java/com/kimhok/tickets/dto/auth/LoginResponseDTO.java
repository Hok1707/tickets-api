package com.kimhok.tickets.dto.auth;

import com.kimhok.tickets.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}