package com.kimhok.tickets.controller;
import com.kimhok.tickets.service.PaymentService;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/bakong/qr/{orderId}")
    public ResponseEntity<KHQRResponse<?>> generateQR(@PathVariable String orderId,
                                                      @RequestBody IndividualInfo individualInfo) {
        log.info("Create QR process {}",orderId);
        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(paymentService.createKhQr(orderId,individualInfo));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/bakong/verify/{qr}")
    public ResponseEntity<KHQRResponse<?>> verifyKhQr(@PathVariable String qr){
        KHQRResponse<CRCValidation> verify = BakongKHQR.verify(qr);
        return ResponseEntity.status(HttpStatus.OK).body(verify);
    }

    @GetMapping("/bakong/decode/{qrCode}")
    public ResponseEntity<KHQRResponse<?>> decodeKhQr(@PathVariable String qrCode){
        KHQRResponse<KHQRDecodeData> decoded = BakongKHQR.decode(qrCode);
        return ResponseEntity.status(HttpStatus.OK).body(decoded);
    }

}
