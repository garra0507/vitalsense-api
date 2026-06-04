package com.biotech.vitalsenseapi.payment.repository;

import com.biotech.vitalsenseapi.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
}