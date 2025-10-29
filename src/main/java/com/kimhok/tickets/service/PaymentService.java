package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.payment.BakongTransactionResponse;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import reactor.core.publisher.Mono;

public interface PaymentService {
    IndividualInfo createKhQr(String orderId,IndividualInfo request);
    Mono<BakongTransactionResponse> checkTransactionStatus(String md5);
}
