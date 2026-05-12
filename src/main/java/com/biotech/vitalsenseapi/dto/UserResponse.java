package com.biotech.vitalsenseapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Long profileId; // This will be either patientId or doctorId
}
