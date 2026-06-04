package com.biotech.vitalsenseapi.prescription.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PrescriptionResponseDTO {

    private Long prescriptionId;

    private Long appointmentId;

    private String medications;

    private String instructions;

    private LocalDateTime createdAt;
}