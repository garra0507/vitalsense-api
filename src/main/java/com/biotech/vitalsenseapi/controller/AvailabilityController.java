package com.biotech.vitalsenseapi.controller;

import com.biotech.vitalsenseapi.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public String createAvailability(
            @RequestBody AvailabilityRequest request
    ) {

        return availabilityService.createAvailability(
                request
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public List<AvailabilityResponse>
    getDoctorAvailability(
            @PathVariable Long doctorId
    ) {

        return availabilityService
                .getDoctorAvailability(
                        doctorId
                );
    }
}