package com.biotech.vitalsenseapi.patient.controller;

import com.biotech.vitalsenseapi.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // Legacy registration endpoint removed.
    // Patient profiles are now created during Auth registration.
}
