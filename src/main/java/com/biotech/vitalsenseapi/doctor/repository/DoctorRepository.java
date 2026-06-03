package com.biotech.vitalsenseapi.doctor.repository;

import com.biotech.vitalsenseapi.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialtyContainingIgnoreCase(
            String specialty
    );
}