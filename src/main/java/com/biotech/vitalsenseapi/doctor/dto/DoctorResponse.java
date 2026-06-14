package com.biotech.vitalsenseapi.doctor.dto;

import com.biotech.vitalsenseapi.doctor.model.Specialty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponse {

    private Long doctorId;

    private String fullName;

    private Specialty specialty;

    private Integer yearsOfExperience;

    private Double consultationFee;

    private String biography;
}