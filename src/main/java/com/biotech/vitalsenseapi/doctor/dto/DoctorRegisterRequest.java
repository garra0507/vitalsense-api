package com.biotech.vitalsenseapi.doctor.dto;

import lombok.Data;

@Data
public class DoctorRegisterRequest {

    private String specialty;

    private Integer yearsOfExperience;

    private Double consultationFee;

    private String biography;
}