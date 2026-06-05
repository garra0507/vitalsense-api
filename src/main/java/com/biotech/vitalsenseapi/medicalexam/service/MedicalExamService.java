package com.biotech.vitalsenseapi.medicalexam.service;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.medicalexam.dto.MedicalExamRequestDTO;
import com.biotech.vitalsenseapi.medicalexam.dto.MedicalExamResponseDTO;
import com.biotech.vitalsenseapi.medicalexam.model.MedicalExam;
import com.biotech.vitalsenseapi.medicalexam.repository.MedicalExamRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalExamService {

    private final MedicalExamRepository medicalExamRepository;

    private final AppointmentRepository appointmentRepository;

    public MedicalExamResponseDTO createMedicalExam(
            MedicalExamRequestDTO request
    ) {

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        )
                );

        MedicalExam medicalExam =
                MedicalExam.builder()
                        .appointment(appointment)
                        .examName(request.getExamName())
                        .instructions(request.getInstructions())
                        .createdAt(LocalDateTime.now())
                        .build();

        MedicalExam savedExam =
                medicalExamRepository.save(medicalExam);

        return mapToResponse(savedExam);
    }

    public List<MedicalExamResponseDTO>
    getMedicalExamsByAppointment(Long appointmentId) {

        return medicalExamRepository
                .findByAppointmentAppointmentId(
                        appointmentId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MedicalExamResponseDTO mapToResponse(
            MedicalExam exam
    ) {

        return MedicalExamResponseDTO.builder()
                .examId(exam.getExamId())
                .appointmentId(
                        exam.getAppointment()
                                .getAppointmentId()
                )
                .examName(exam.getExamName())
                .instructions(exam.getInstructions())
                .createdAt(exam.getCreatedAt())
                .build();
    }
}