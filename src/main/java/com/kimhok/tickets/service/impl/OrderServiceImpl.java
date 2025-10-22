package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;
import com.kimhok.tickets.entity.Order;
import com.kimhok.tickets.entity.OrderItem;
import com.kimhok.tickets.mapper.OrderMapper;
import com.kimhok.tickets.repository.OrderRepository;
import com.kimhok.tickets.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {
        Order order = orderMapper.toEntity(request);
        var items = request.getItems().stream().map(i ->
                OrderItem.builder()
                        .eventName(i.getEventName())
                        .ticketName(i.getTicketName())
                        .quantity(i.getQuantity())
                        .unitPrice(BigDecimal.valueOf(i.getPrice()))
                        .order(order)
                        .build()
        ).toList();
        order.setItems(items);
        order.setTotalAmount(items.stream().map(
                it->it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add)
        );
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }
}
