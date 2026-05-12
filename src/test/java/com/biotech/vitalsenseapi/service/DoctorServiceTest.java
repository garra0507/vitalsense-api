package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.DoctorResponse;
import com.biotech.vitalsenseapi.model.Doctor;
import com.biotech.vitalsenseapi.model.User;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void searchDoctors_Success() {
        User user = User.builder().firstName("John").lastName("Doe").build();
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1L);
        doctor.setUser(user);
        doctor.setSpecialty("Cardiology");

        when(doctorRepository.findBySpecialtyContainingIgnoreCase("Cardiology")).thenReturn(List.of(doctor));

        List<DoctorResponse> responses = doctorService.searchDoctors("Cardiology");

        assertFalse(responses.isEmpty());
        assertEquals("John Doe", responses.get(0).getFullName());
        assertEquals("Cardiology", responses.get(0).getSpecialty());
    }
}
