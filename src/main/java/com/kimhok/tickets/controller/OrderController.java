package com.kimhok.tickets.controller;

import com.kimhok.tickets.common.utils.ApiResponse;
import com.kimhok.tickets.dto.OrderResponse;
import com.kimhok.tickets.dto.order.UpdateOrderStatusRequest;
import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;
import com.kimhok.tickets.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        log.info("Checkout request received: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Checkout Successfully",
                        orderService.checkout(request)
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> findOrderById(@PathVariable String orderId) {
        log.info("Enquiry order By Id");
        var response = orderService.findOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK.value(), "Enquiry Order Successfully", response));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateOrderStatusRequest request){
        log.info("Update order status {} ",orderId);
        orderService.updateOrderStatus(orderId,request);
        return ResponseEntity.status(HttpStatus.OK).body("Order Paid Success");
    }
}
