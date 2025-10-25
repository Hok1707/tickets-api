package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.service.PaymentService;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    @Transactional
    public IndividualInfo createKhQr(String orderId, IndividualInfo request) {
        IndividualInfo individualInfo = new IndividualInfo();
        individualInfo.setBakongAccountId("heng_kimhok@lolc");
        individualInfo.setAccountInformation("+85589456299");
        individualInfo.setStoreLabel("HokEvent");
        individualInfo.setCurrency(KHQRCurrency.valueOf(String.valueOf(request.getCurrency())));
        individualInfo.setAmount(request.getAmount());
        individualInfo.setBillNumber(request.getBillNumber());
        individualInfo.setMerchantName("Heng Kimhok");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long expirationTimestamp = calendar.getTimeInMillis();
        individualInfo.setExpirationTimestamp(expirationTimestamp);
        return individualInfo;
    }

}