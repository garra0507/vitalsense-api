package com.biotech.vitalsenseapi.controller;

import com.biotech.vitalsenseapi.dto.LoginRequest;
import com.biotech.vitalsenseapi.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.dto.UserResponse;
import com.biotech.vitalsenseapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "FUNCIONA";
    }

    @PostMapping("/register/patient")
    public String registerPatient(
            @RequestBody PatientRegisterRequest request
    ) {
        return authService.registerPatient(request);
    }

    @PostMapping("/register/doctor")
    public String registerDoctor(
            @RequestBody DoctorRegisterRequest request
    ) {
        return authService.registerDoctor(request);
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse getMe() {
        return authService.getMe();
    }
}