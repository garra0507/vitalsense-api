package com.biotech.vitalsenseapi.patient.repository;

import com.biotech.vitalsenseapi.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {
}