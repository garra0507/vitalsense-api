package com.biotech.vitalsenseapi.medicalexam.controller;

import com.biotech.vitalsenseapi.medicalexam.dto.MedicalExamRequestDTO;
import com.biotech.vitalsenseapi.medicalexam.dto.MedicalExamResponseDTO;
import com.biotech.vitalsenseapi.medicalexam.service.MedicalExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-exams")
@RequiredArgsConstructor
public class MedicalExamController {

    private final MedicalExamService medicalExamService;

    @PostMapping
    public ResponseEntity<MedicalExamResponseDTO>
    createMedicalExam(
            @RequestBody
            MedicalExamRequestDTO request
    ) {

        return new ResponseEntity<>(
                medicalExamService
                        .createMedicalExam(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<MedicalExamResponseDTO>>
    getMedicalExamsByAppointment(
            @PathVariable Long appointmentId
    ) {

        return ResponseEntity.ok(
                medicalExamService
                        .getMedicalExamsByAppointment(
                                appointmentId
                        )
        );
    }
}