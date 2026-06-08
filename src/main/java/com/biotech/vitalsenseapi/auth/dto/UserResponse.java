package com.biotech.vitalsenseapi.auth.dto;

import com.biotech.vitalsenseapi.auth.model.Role;
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
    private Role role;
    private Long profileId; // patientId or doctorId
}
