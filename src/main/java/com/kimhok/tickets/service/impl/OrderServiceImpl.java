package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.utils.AuthUtil;
import com.kimhok.tickets.common.utils.BillUtil;
import com.kimhok.tickets.dto.OrderResponse;
import com.kimhok.tickets.dto.order.UpdateOrderStatusRequest;
import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;
import com.kimhok.tickets.entity.Order;
import com.kimhok.tickets.entity.OrderItem;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.mapper.OrderMapper;
import com.kimhok.tickets.repository.EventRepository;
import com.kimhok.tickets.repository.OrderRepository;
import com.kimhok.tickets.repository.TicketTypeRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final AuthUtil authUtil;

    private static final BigDecimal TRANSACTION_FEE_RATE = new BigDecimal("0.05");

    @Transactional
    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = Order.builder()
                .billNumber(BillUtil.generateBillNumber())
                .user(user)
                .paymentMethod(request.getPaymentMethod())
                .build();

        List<OrderItem> items = request.getItems().stream().map(i -> {
            var event = eventRepository.findById(i.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
            var ticket = ticketTypeRepository.findByIdWithLock(i.getTicketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found"));

            return OrderItem.builder()
                    .eventId(event.getId())
                    .ticketType(ticket.getId())
                    .unitPrice(BigDecimal.valueOf(ticket.getPrice()))
                    .quantity(i.getQuantity())
                    .order(order)
                    .build();
        }).toList();

        order.setItems(items);

        BigDecimal subtotal = items.stream()
                .map(it -> it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal transactionFee = subtotal.multiply(TRANSACTION_FEE_RATE);
        BigDecimal totalAmount = subtotal.add(transactionFee);

        order.setSubtotal(subtotal);
        order.setTransactionFee(transactionFee);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse findOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order Id not found! " + orderId)
        );
        if (order != null) {
            String purchaser = order.getUser().getId();
            String currentUser = authUtil.getCurrentUserId();
            if (!currentUser.equals(purchaser)) {
                throw new ResourceNotFoundException("This order is not belong to this user " + currentUser);
            }
        }
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        order.setStatus(request.getStatus());
        order.setMd5Hash(request.getMd5Hash());
        orderRepository.save(order);
    }
}