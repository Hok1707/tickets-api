package com.kimhok.tickets.dto.order;

import com.kimhok.tickets.common.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
    private String md5Hash;
}
