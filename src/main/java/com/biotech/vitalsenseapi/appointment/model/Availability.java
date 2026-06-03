package com.biotech.vitalsenseapi.appointment.model;

import com.biotech.vitalsenseapi.doctor.model.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availabilityId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}