package com.biotech.vitalsenseapi.prescription.dto;

import lombok.Data;

@Data
public class PrescriptionRequestDTO {

    private Long appointmentId;

    private String medications;

    private String instructions;
}