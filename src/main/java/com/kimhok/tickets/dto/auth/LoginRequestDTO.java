package com.kimhok.tickets.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Email are required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password are required")
    private String password;
}