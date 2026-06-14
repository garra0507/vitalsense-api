package com.biotech.vitalsenseapi.doctor.controller;

import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.model.Specialty;
import com.biotech.vitalsenseapi.doctor.service.DoctorService;
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
            @RequestParam Specialty specialty
    ) {
        return doctorService.searchDoctors(specialty);
    }
}
