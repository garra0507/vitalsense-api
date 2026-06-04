package com.biotech.vitalsenseapi.payment.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {

    private Long appointmentId;

    private Double amount;

    private String paymentMethod;
}