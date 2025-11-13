package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.dto.payment.BakongTransactionResponse;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.service.PaymentService;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WebClient webClient;

    @Value("${bakong.token}")
    private String bakongToken;
    @Value("${bakong.base-uri}")
    private String bakongURI;
    private static final String CHECK_MD5 = "/check_transaction_by_md5";

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
        calendar.add(Calendar.MINUTE, 2);
        long expirationTimestamp = calendar.getTimeInMillis();
        individualInfo.setExpirationTimestamp(expirationTimestamp);
        return individualInfo;
    }

    @Override
    public Mono<BakongTransactionResponse> checkTransactionStatus(String md5) {
        return webClient.post()
                .uri(bakongURI + CHECK_MD5)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bakongToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Map.of("md5", md5))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Bakong API HTTP error: " + body)))
                )
                .bodyToMono(BakongTransactionResponse.class)
                .map(resp -> {
                    log.info("Bakong response for md5 {} → code={}, msg={}", md5, resp.getResponseCode(), resp.getResponseMessage());
                    if (resp.getResponseCode() == 0) {
                        resp.setStatus("SUCCESS");
                    } else if (resp.getResponseCode() == 1) {
                        resp.setStatus("PENDING");
                    } else if (resp.getErrorCode() == 3) {
                        resp.setStatus("FAILED");
                    } else {
                        resp.setStatus("UNKNOWN");
                    }

                    return resp;
                })
                .doOnNext(r -> log.debug("Bakong status for md5={} → {}", md5, r.getStatus()))
                .doOnError(err -> log.error("Error checking Bakong tx for md5={}", md5, err));
    }

}