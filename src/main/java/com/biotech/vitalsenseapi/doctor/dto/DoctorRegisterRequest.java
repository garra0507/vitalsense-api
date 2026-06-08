package com.biotech.vitalsenseapi.doctor.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorRegisterRequest {
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

    // Doctor profile info
    @NotBlank(message = "Specialty is required")
    private String specialty;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    @Positive(message = "Consultation fee must be positive")
    private Double consultationFee;

    private String biography;
}
