package com.biotech.vitalsenseapi.prescription.repository;

import com.biotech.vitalsenseapi.prescription.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {
}