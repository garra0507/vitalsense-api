package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.model.Availability;
import com.biotech.vitalsenseapi.model.Doctor;
import com.biotech.vitalsenseapi.repository.AvailabilityRepository;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    private final DoctorRepository doctorRepository;

    public String createAvailability(
            AvailabilityRequest request
    ) {

        Doctor doctor = doctorRepository.findById(
                request.getDoctorId()
        ).orElseThrow(
                () -> new RuntimeException("Doctor not found")
        );

        Availability availability =
                Availability.builder()
                        .doctor(doctor)
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .available(true)
                        .build();

        availabilityRepository.save(availability);

        return "Availability created";
    }

    public List<AvailabilityResponse> getDoctorAvailability(
            Long doctorId
    ) {

        return availabilityRepository
                .findByDoctorDoctorId(doctorId)
                .stream()
                .map(a -> AvailabilityResponse.builder()
                        .availabilityId(
                                a.getAvailabilityId()
                        )
                        .startTime(a.getStartTime())
                        .endTime(a.getEndTime())
                        .available(a.getAvailable())
                        .build()
                )
                .toList();
    }
}