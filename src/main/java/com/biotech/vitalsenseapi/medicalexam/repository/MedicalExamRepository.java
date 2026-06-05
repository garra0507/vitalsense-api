package com.biotech.vitalsenseapi.medicalexam.repository;

import com.biotech.vitalsenseapi.medicalexam.model.MedicalExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalExamRepository
        extends JpaRepository<MedicalExam, Long> {

    List<MedicalExam>
    findByAppointmentAppointmentId(Long appointmentId);
}