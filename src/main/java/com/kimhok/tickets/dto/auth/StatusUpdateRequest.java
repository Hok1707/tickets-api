package com.kimhok.tickets.dto.auth;

import com.kimhok.tickets.common.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateRequest {
    private UserStatus status;
}
