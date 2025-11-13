package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.OrderResponse;
import com.kimhok.tickets.dto.order.UpdateOrderStatusRequest;
import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;

public interface OrderService {
    CheckoutResponse checkout(CheckoutRequest request);
    OrderResponse findOrderById(String orderId);
    void updateOrderStatus(String orderId, UpdateOrderStatusRequest request);

}
