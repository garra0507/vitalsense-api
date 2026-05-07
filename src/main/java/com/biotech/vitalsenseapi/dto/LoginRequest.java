package com.biotech.vitalsenseapi.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}