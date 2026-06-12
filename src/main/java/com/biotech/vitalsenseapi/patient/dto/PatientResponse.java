package com.biotech.vitalsenseapi.patient.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientResponse {

    private Long patientId;

    private String fullName;

    private Integer age;

    private String gender;

    private Double balance;

    private String emergencyContact;
}