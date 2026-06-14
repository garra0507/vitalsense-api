package com.biotech.vitalsenseapi.appointment.service;

import com.biotech.vitalsenseapi.appointment.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.dto.BatchAvailabilityRequest;
import com.biotech.vitalsenseapi.appointment.dto.TimeSlotRequest;
import com.biotech.vitalsenseapi.appointment.mapper.AvailabilityMapper;
import com.biotech.vitalsenseapi.appointment.model.Availability;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.appointment.repository.AvailabilityRepository;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilityMapper availabilityMapper;

    @Transactional
    public String createBatchAvailability(BatchAvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Availability> currentAvailabilities = availabilityRepository.findByDoctorDoctorId(doctor.getDoctorId());

        for (TimeSlotRequest slot : request.getSlots()) {
            // Check for overlaps within the new request itself or existing ones
            boolean overlaps = request.getSlots().stream()
                    .filter(s -> s != slot)
                    .anyMatch(s -> slot.getStartTime().isBefore(s.getEndTime()) && slot.getEndTime().isAfter(s.getStartTime()))
                    ||
                    currentAvailabilities.stream()
                    .anyMatch(a -> slot.getStartTime().isBefore(a.getEndTime()) && slot.getEndTime().isAfter(a.getStartTime()));

            if (overlaps) {
                throw new ValidationException("Existen bloques horarios que se solapan para el " + 
                    slot.getStartTime().toLocalDate());
            }

            Availability availability = Availability.builder()
                    .doctor(doctor)
                    .startTime(slot.getStartTime())
                    .endTime(slot.getEndTime())
                    .available(true)
                    .build();

            availabilityRepository.save(availability);
        }

        return "Horarios actualizados exitosamente";
    }

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
