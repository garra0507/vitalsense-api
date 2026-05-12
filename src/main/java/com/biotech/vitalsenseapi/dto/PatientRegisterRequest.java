package com.biotech.vitalsenseapi.dto;

import lombok.Data;

@Data
public class PatientRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    
    // Patient specific fields
    private Integer age;
    private String gender;
    private String emergencyContact;
}
