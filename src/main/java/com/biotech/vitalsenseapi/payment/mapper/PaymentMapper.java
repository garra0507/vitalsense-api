package com.biotech.vitalsenseapi.payment.mapper;

import com.biotech.vitalsenseapi.payment.dto.PaymentResponseDTO;
import com.biotech.vitalsenseapi.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .appointmentId(payment.getAppointment().getAppointmentId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentDate(payment.getPaymentDate())
                .status(payment.getStatus())
                .build();
    }
}
