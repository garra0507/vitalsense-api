package com.biotech.vitalsenseapi.prescription.repository;

import com.biotech.vitalsenseapi.prescription.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointmentAppointmentId(Long appointmentId);
}