package com.kimhok.tickets.service;

import kh.gov.nbc.bakong_khqr.model.IndividualInfo;

public interface PaymentService {
    IndividualInfo createKhQr(IndividualInfo request);
}
