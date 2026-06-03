package com.biotech.vitalsenseapi.doctor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponse {

    private Long doctorId;

    private String fullName;

    private String specialty;

    private Integer yearsOfExperience;

    private Double consultationFee;

    private String biography;
}