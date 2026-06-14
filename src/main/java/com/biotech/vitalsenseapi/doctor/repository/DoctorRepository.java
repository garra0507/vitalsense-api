package com.biotech.vitalsenseapi.doctor.repository;

import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialty(Specialty specialty);

    Optional<Doctor> findByUserUserId(Long userId);
}
