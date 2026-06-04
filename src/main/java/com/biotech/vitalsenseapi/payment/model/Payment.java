package com.biotech.vitalsenseapi.payment.model;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private Double amount;

    private String paymentMethod;

    private LocalDateTime paymentDate;

    private String status;
}