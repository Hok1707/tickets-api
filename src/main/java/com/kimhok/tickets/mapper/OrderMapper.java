package com.kimhok.tickets.mapper;

import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;
import com.kimhok.tickets.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "items")
    Order toEntity(CheckoutRequest request);
    @Mapping(source = "id", target = "orderId")
    CheckoutResponse toResponse(Order order);
}