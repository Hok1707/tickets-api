package com.kimhok.tickets.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ChangePasswordRequestDTO {
    @NotBlank(message = "Current Password are required!")
    private String currentPassword;
    @NotBlank(message = "New Password are required!")
    private String newPassword;
    @NotBlank(message = "Confirm Password are required!")
    private String confirmPassword;
}
