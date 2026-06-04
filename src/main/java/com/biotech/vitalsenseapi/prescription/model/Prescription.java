package com.biotech.vitalsenseapi.prescription.model;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false, length = 1000)
    private String medications;

    @Column(nullable = false, length = 1000)
    private String instructions;

    private LocalDateTime createdAt;
}