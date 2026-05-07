package com.biotech.vitalsenseapi.controller;

import com.biotech.vitalsenseapi.dto.DoctorResponse;
import com.biotech.vitalsenseapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/search")
    public List<DoctorResponse> searchDoctors(
            @RequestParam String specialty
    ) {

        return doctorService.searchDoctors(
                specialty
        );
    }
}