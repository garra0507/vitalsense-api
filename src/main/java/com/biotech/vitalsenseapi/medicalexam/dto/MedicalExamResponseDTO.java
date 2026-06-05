package com.biotech.vitalsenseapi.medicalexam.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalExamResponseDTO {

    private Long examId;

    private Long appointmentId;

    private String examName;

    private String instructions;

    private LocalDateTime createdAt;
}