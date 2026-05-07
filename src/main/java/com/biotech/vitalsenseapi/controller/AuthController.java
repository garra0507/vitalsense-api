package com.biotech.vitalsenseapi.controller;

import com.biotech.vitalsenseapi.dto.LoginRequest;
import com.biotech.vitalsenseapi.dto.RegisterRequest;
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

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
}