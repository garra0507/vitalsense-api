package com.biotech.vitalsenseapi.repository;

import com.biotech.vitalsenseapi.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUserId(Long userId);
}