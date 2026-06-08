package com.biotech.vitalsenseapi.medicalexam.mapper;

import com.biotech.vitalsenseapi.medicalexam.dto.MedicalExamResponseDTO;
import com.biotech.vitalsenseapi.medicalexam.model.MedicalExam;
import org.springframework.stereotype.Component;

@Component
public class MedicalExamMapper {

    public MedicalExamResponseDTO toResponseDTO(MedicalExam exam) {
        if (exam == null) {
            return null;
        }
        return MedicalExamResponseDTO.builder()
                .examId(exam.getExamId())
                .appointmentId(exam.getAppointment().getAppointmentId())
                .examName(exam.getExamName())
                .instructions(exam.getInstructions())
                .createdAt(exam.getCreatedAt())
                .build();
    }
}
