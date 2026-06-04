package com.biotech.vitalsenseapi.patient.dto;

import lombok.Data;

@Data
public class PatientRegisterRequest {

    private Integer age;

    private String gender;

    private String emergencyContact;
}