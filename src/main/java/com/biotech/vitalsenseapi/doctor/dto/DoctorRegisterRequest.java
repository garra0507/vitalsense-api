package com.biotech.vitalsenseapi.doctor.dto;

import lombok.Data;

@Data
public class DoctorRegisterRequest {
    // User account info
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    // Doctor profile info
    private String specialty;
    private Integer yearsOfExperience;
    private Double consultationFee;
    private String biography;
}
