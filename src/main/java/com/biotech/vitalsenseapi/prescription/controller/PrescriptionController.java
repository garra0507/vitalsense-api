package com.biotech.vitalsenseapi.prescription.controller;

import com.biotech.vitalsenseapi.prescription.dto.PrescriptionRequestDTO;
import com.biotech.vitalsenseapi.prescription.dto.PrescriptionResponseDTO;
import com.biotech.vitalsenseapi.prescription.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @RequestBody PrescriptionRequestDTO request
    ) {

        return new ResponseEntity<>(
                prescriptionService.createPrescription(request),
                HttpStatus.CREATED
        );
    }
}