package com.biotech.vitalsenseapi.dto;

import lombok.Data;

@Data
public class DoctorRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    // Doctor specific fields
    private String specialty;
    private Integer yearsOfExperience;
    private Double consultationFee;
    private String biography;
}
