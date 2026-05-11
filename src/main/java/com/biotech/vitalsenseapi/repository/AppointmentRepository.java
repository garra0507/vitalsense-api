package com.biotech.vitalsenseapi.repository;

import com.biotech.vitalsenseapi.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByDoctorDoctorIdAndScheduledDate(Long doctorId, LocalDateTime scheduledDate);
    List<Appointment> findByPatientPatientId(Long patientId);
    List<Appointment> findByDoctorDoctorId(Long doctorId);
}
