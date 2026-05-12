package com.biotech.vitalsenseapi.repository;

import com.biotech.vitalsenseapi.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialtyContainingIgnoreCase(
            String specialty
    );

    Optional<Doctor> findByUserUserId(Long userId);
}