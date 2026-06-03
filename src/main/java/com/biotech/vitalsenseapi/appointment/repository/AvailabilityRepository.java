package com.biotech.vitalsenseapi.appointment.repository;

import com.biotech.vitalsenseapi.appointment.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository
        extends JpaRepository<Availability, Long> {

    List<Availability> findByDoctorDoctorId(
            Long doctorId
    );
}