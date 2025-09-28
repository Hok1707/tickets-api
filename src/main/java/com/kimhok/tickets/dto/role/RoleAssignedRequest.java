package com.kimhok.tickets.dto.role;

import lombok.Data;

@Data
public class RoleAssignedRequest {
    private String userEmail;
    private String roleName;
}
