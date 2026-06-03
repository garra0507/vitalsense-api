package com.biotech.vitalsenseapi.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}