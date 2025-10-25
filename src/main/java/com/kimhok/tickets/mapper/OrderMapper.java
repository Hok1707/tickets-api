package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.OrderResponse;
import com.kimhok.tickets.dto.payment.CheckoutResponse;
import com.kimhok.tickets.entity.Order;
import com.kimhok.tickets.entity.OrderItem;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status",expression = "java(order.getStatus().name())")
    @Mapping(target = "userId", source = "user.id")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "billNumber", expression = "java(\"EVT-\" + order.getId())")
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "amount", source = "totalAmount")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "transactionFee", source = "transactionFee")
    CheckoutResponse toResponse(Order order);
    @Mapping(target = "totalPrice", expression = "java(calcTotal(item))")
    CheckoutResponse.OrderItemResponse toItemResponse(OrderItem item);
    default BigDecimal calcTotal(OrderItem i) {
        return i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
    }
}