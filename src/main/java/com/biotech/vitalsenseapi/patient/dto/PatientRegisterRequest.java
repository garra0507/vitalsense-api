package com.biotech.vitalsenseapi.patient.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PatientRegisterRequest {
    // User account info
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    // Patient profile info
    @Min(value = 0, message = "Age cannot be negative")
    private Integer age;

    private String gender;

    @Min(value = 0, message = "Balance cannot be negative")
    private Double balance;

    private String emergencyContact;
}
