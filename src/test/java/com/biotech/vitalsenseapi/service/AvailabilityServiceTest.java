package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.AvailabilityRequest;
import com.biotech.vitalsenseapi.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.model.Availability;
import com.biotech.vitalsenseapi.model.Doctor;
import com.biotech.vitalsenseapi.repository.AvailabilityRepository;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void createAvailability_Success() {
        AvailabilityRequest request = new AvailabilityRequest();
        request.setDoctorId(1L);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(1));

        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        String result = availabilityService.createAvailability(request);

        assertEquals("Availability created", result);
        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }

    @Test
    void getDoctorAvailability_Success() {
        Availability availability = Availability.builder()
                .availabilityId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .available(true)
                .build();

        when(availabilityRepository.findByDoctorDoctorId(1L)).thenReturn(List.of(availability));

        List<AvailabilityResponse> responses = availabilityService.getDoctorAvailability(1L);

        assertFalse(responses.isEmpty());
        assertEquals(1L, responses.get(0).getAvailabilityId());
    }
}
