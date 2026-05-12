package com.biotech.vitalsenseapi.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;

    // Patient specific fields
    private Integer age;
    private String gender;
    private String emergencyContact;

    // Doctor specific fields
    private String specialty;
    private Integer yearsOfExperience;
    private Double consultationFee;
    private String biography;
}