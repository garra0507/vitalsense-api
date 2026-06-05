package com.biotech.vitalsenseapi.medicalexam.dto;

import lombok.Data;

@Data
public class MedicalExamRequestDTO {

    private Long appointmentId;

    private String examName;

    private String instructions;
}