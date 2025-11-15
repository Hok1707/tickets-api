package com.kimhok.tickets.service;

import com.kimhok.tickets.common.utils.PagedResponse;
import com.kimhok.tickets.dto.OrderResponse;
import com.kimhok.tickets.dto.order.OrderDashboardResponse;
import com.kimhok.tickets.dto.order.UpdateOrderStatusRequest;
import com.kimhok.tickets.dto.order.UpdateStatusRequest;
import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;

public interface OrderService {
    CheckoutResponse checkout(CheckoutRequest request);
    OrderResponse findOrderById(String orderId);
    void updateOrderStatus(String orderId, UpdateOrderStatusRequest request);
    PagedResponse<OrderResponse> getAllOrder(int page,int size,String sortBy,String direction );
    void updateStatus(String orderId, UpdateStatusRequest request);
    OrderDashboardResponse getOrderFinancial();
}
