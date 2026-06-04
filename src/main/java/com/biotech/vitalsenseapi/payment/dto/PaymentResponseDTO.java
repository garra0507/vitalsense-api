package com.biotech.vitalsenseapi.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {

    private Long paymentId;

    private Long appointmentId;

    private Double amount;

    private String paymentMethod;

    private LocalDateTime paymentDate;

    private String status;
}