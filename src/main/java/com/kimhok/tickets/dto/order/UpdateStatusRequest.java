package com.kimhok.tickets.dto.order;

import com.kimhok.tickets.common.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private OrderStatus status;
}
