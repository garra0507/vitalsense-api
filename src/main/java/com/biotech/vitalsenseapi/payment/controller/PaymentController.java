package com.biotech.vitalsenseapi.payment.controller;

import com.biotech.vitalsenseapi.payment.dto.PaymentRequestDTO;
import com.biotech.vitalsenseapi.payment.dto.PaymentResponseDTO;
import com.biotech.vitalsenseapi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @RequestBody PaymentRequestDTO request
    ) {

        return new ResponseEntity<>(
                paymentService.processPayment(request),
                HttpStatus.CREATED
        );
    }
}