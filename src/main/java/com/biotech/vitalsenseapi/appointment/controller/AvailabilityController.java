package com.biotech.vitalsenseapi.appointment.controller;

import com.biotech.vitalsenseapi.appointment.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.dto.BatchAvailabilityRequest;
import com.biotech.vitalsenseapi.appointment.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/batch")
    public String createBatchAvailability(
            @RequestBody BatchAvailabilityRequest request
    ) {
        return availabilityService.createBatchAvailability(request);
    }

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