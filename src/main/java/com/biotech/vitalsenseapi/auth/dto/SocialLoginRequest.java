package com.biotech.vitalsenseapi.auth.dto;

import lombok.Data;

@Data
public class SocialLoginRequest {

    private String provider;

    private String email;

    private String firstName;

    private String lastName;
}