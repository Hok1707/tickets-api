package com.kimhok.tickets.controller;

import com.kimhok.tickets.dto.payment.BakongTransactionResponse;
import com.kimhok.tickets.service.PaymentService;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments/bakong")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/qr/{orderId}")
    public ResponseEntity<KHQRResponse<?>> generateQR(@PathVariable String orderId,
                                                      @RequestBody IndividualInfo individualInfo) {
        log.info("Create QR process {}", orderId);
        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(paymentService.createKhQr(orderId, individualInfo));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/verify/{qr}")
    public ResponseEntity<KHQRResponse<?>> verifyKhQr(@PathVariable String qr) {
        KHQRResponse<CRCValidation> verify = BakongKHQR.verify(qr);
        return ResponseEntity.status(HttpStatus.OK).body(verify);
    }

    @GetMapping("/decode/{qrCode}")
    public ResponseEntity<KHQRResponse<?>> decodeKhQr(@PathVariable String qrCode) {
        KHQRResponse<KHQRDecodeData> decoded = BakongKHQR.decode(qrCode);
        return ResponseEntity.status(HttpStatus.OK).body(decoded);
    }

    @PostMapping("/check-md5")
    public Mono<ResponseEntity<BakongTransactionResponse>> checkMd5(@RequestBody Map<String, String> request) {
        String md5 = request.get("md5");

        if (md5 == null || md5.isBlank()) {
            return Mono.just(ResponseEntity
                    .badRequest()
                    .body(BakongTransactionResponse.builder()
                            .status("ERROR")
                            .responseMessage("Missing MD5 parameter")
                            .build()));
        }

        return paymentService.checkTransactionStatus(md5)
                .map(response -> {
                    if ("SUCCESS".equals(response.getStatus())) {
                        return ResponseEntity.ok(response);
                    } else if ("PENDING".equals(response.getStatus())) {
                        return ResponseEntity.ok(response);
                    } else if ("FAILED".equals(response.getStatus())) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.ok(BakongTransactionResponse.builder()
                                .status("UNKNOWN")
                                .responseMessage("Unhandled Bakong status")
                                .build());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error checking Bakong transaction for MD5 {}: {}", md5, e.getMessage());
                    return Mono.just(ResponseEntity.ok(BakongTransactionResponse.builder()
                            .status("ERROR")
                            .responseMessage("Bakong service temporarily unavailable: " + e.getMessage())
                            .build()));
                });
    }
}
