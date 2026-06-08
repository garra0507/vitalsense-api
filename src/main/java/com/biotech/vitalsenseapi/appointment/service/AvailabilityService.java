package com.biotech.vitalsenseapi.appointment.service;

import com.biotech.vitalsenseapi.appointment.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.mapper.AvailabilityMapper;
import com.biotech.vitalsenseapi.appointment.model.Availability;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.appointment.repository.AvailabilityRepository;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilityMapper availabilityMapper;

    public String createAvailability(
            AvailabilityRequest request
    ) {

        Doctor doctor = doctorRepository.findById(
                request.getDoctorId()
        ).orElseThrow(
                () -> new RuntimeException("Doctor not found")
        );

        // Logic Gap Fix: Check for overlapping availability
        List<Availability> existingAvailabilities = availabilityRepository.findByDoctorDoctorId(doctor.getDoctorId());
        boolean overlaps = existingAvailabilities.stream().anyMatch(a -> 
            (request.getStartTime().isBefore(a.getEndTime()) && request.getEndTime().isAfter(a.getStartTime()))
        );

        if (overlaps) {
            throw new ValidationException("This availability slot overlaps with an existing one.");
        }

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
                .map(availabilityMapper::toResponseDTO)
                .toList();
    }
}
