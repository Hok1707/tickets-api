package com.kimhok.tickets.controller;
import com.kimhok.tickets.service.PaymentService;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/bakong/qr")
    public ResponseEntity<KHQRResponse<?>> generateQR(@RequestBody IndividualInfo individualInfo) {
        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(paymentService.createKhQr(individualInfo));
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
