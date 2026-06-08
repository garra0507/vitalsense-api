package com.biotech.vitalsenseapi.patient.dto;

import lombok.Data;

@Data
public class PatientRegisterRequest {
    // User account info
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    // Patient profile info
    private Integer age;
    private String gender;
    private String emergencyContact;
}
