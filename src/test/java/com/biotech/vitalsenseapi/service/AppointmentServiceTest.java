package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.exception.AppointmentConflictException;
import com.biotech.vitalsenseapi.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.exception.ValidationException;
import com.biotech.vitalsenseapi.model.*;
import com.biotech.vitalsenseapi.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import com.biotech.vitalsenseapi.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequestDTO requestDTO;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        requestDTO = new AppointmentRequestDTO();
        requestDTO.setPatientId(1L);
        requestDTO.setDoctorId(1L);
        requestDTO.setScheduledDate(LocalDateTime.now().plusDays(1));

        patient = new Patient();
        patient.setPatientId(1L);

        doctor = new Doctor();
        doctor.setDoctorId(1L);
    }

    @Test
    void scheduleAppointment_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorDoctorIdAndScheduledDate(any(), any())).thenReturn(Optional.empty());
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentResponseDTO response = appointmentService.scheduleAppointment(requestDTO);

        assertNotNull(response);
        assertEquals(AppointmentStatus.PENDING, response.getStatus());
        assertNotNull(response.getMeetLink());
        assertTrue(response.getMeetLink().startsWith("https://meet.vitalsense.com/"));
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void scheduleAppointment_PastDate_ThrowsValidationException() {
        requestDTO.setScheduledDate(LocalDateTime.now().minusDays(1));

        assertThrows(ValidationException.class, () -> appointmentService.scheduleAppointment(requestDTO));
    }

    @Test
    void scheduleAppointment_DoctorNotFound_ThrowsResourceNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.scheduleAppointment(requestDTO));
    }

    @Test
    void scheduleAppointment_DoubleBooking_ThrowsConflictException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorDoctorIdAndScheduledDate(any(), any())).thenReturn(Optional.of(new Appointment()));

        assertThrows(AppointmentConflictException.class, () -> appointmentService.scheduleAppointment(requestDTO));
    }
}
