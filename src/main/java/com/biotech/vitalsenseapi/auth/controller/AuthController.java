package com.biotech.vitalsenseapi.auth.controller;

import com.biotech.vitalsenseapi.auth.dto.LoginRequest;
import com.biotech.vitalsenseapi.auth.dto.SocialLoginRequest;
import com.biotech.vitalsenseapi.auth.service.AuthService;
import com.biotech.vitalsenseapi.doctor.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.patient.dto.PatientRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registerPatient(
            @RequestBody PatientRegisterRequest request
    ) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<String> registerDoctor(
            @RequestBody DoctorRegisterRequest request
    ) {
        return ResponseEntity.ok(authService.registerDoctor(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/social-login")
    public ResponseEntity<String> socialLogin(
            @RequestBody
            SocialLoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.socialLogin(request)
        );
    }
}