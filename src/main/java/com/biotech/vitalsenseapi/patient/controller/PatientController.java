package com.biotech.vitalsenseapi.patient.controller;

import com.biotech.vitalsenseapi.patient.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.patient.dto.PatientResponse;
import com.biotech.vitalsenseapi.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public PatientResponse createPatient(
            @RequestBody PatientRegisterRequest request
    ) {

        return patientService.createPatient(request);
    }
}