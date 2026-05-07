package com.biotech.vitalsenseapi.repository;

import com.biotech.vitalsenseapi.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {
}