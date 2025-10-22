package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.payment.CheckoutRequest;
import com.kimhok.tickets.dto.payment.CheckoutResponse;

public interface OrderService {
    CheckoutResponse checkout(CheckoutRequest request);
}
