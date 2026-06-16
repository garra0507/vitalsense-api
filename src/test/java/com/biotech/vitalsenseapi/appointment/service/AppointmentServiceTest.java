package com.biotech.vitalsenseapi.appointment.service;

import com.biotech.vitalsenseapi.appointment.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.dto.RescheduleAppointmentDTO;
import com.biotech.vitalsenseapi.appointment.exception.AppointmentConflictException;
import com.biotech.vitalsenseapi.appointment.mapper.AppointmentMapper;
import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.model.AppointmentPaymentStatus;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.auth.model.Role;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
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
    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AppointmentService appointmentService;

    private User mockUser;
    private Patient mockPatient;
    private Doctor mockDoctor;
    private Appointment mockAppointment;
    private AppointmentRequestDTO mockRequestDTO;
    private AppointmentResponseDTO mockResponseDTO;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .userId(1L)
                .email("test@gmail.com")
                .role(Role.PATIENT)
                .build();

        mockPatient = Patient.builder()
                .patientId(1L)
                .user(mockUser)
                .build();

        User doctorUser = User.builder()
                .userId(2L)
                .email("doctor@gmail.com")
                .role(Role.DOCTOR)
                .build();

        mockDoctor = Doctor.builder()
                .doctorId(1L)
                .user(doctorUser)
                .build();

        mockAppointment = Appointment.builder()
                .appointmentId(1L)
                .patient(mockPatient)
                .doctor(mockDoctor)
                .scheduledDate(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.PENDING)
                .build();

        mockRequestDTO = new AppointmentRequestDTO();
        mockRequestDTO.setPatientId(1L);
        mockRequestDTO.setDoctorId(1L);
        mockRequestDTO.setScheduledDate(LocalDateTime.now().plusDays(1));
        mockRequestDTO.setPaymentAmount(100.0);

        mockResponseDTO = new AppointmentResponseDTO();
        mockResponseDTO.setAppointmentId(1L);
        mockResponseDTO.setStatus(AppointmentStatus.PENDING);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void setupSecurityContext(String email) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
    }

    @Test
    void scheduleAppointment_Success() {
        setupSecurityContext("test@gmail.com");
        
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(mockDoctor));
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
        when(appointmentRepository.findByDoctorDoctorIdAndScheduledDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        when(appointmentMapper.toResponseDTO(any(Appointment.class))).thenReturn(mockResponseDTO);

        AppointmentResponseDTO result = appointmentService.scheduleAppointment(mockRequestDTO);

        assertNotNull(result);
        assertEquals(AppointmentStatus.PENDING, result.getStatus());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void scheduleAppointment_PastDate_ThrowsValidationException() {
        mockRequestDTO.setScheduledDate(LocalDateTime.now().minusDays(1));

        assertThrows(ValidationException.class, () -> appointmentService.scheduleAppointment(mockRequestDTO));
    }

    @Test
    void scheduleAppointment_DoubleBooking_ThrowsConflictException() {
        setupSecurityContext("test@gmail.com");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(mockDoctor));
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
        when(appointmentRepository.findByDoctorDoctorIdAndScheduledDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(mockAppointment)); // Conflict

        assertThrows(AppointmentConflictException.class, () -> appointmentService.scheduleAppointment(mockRequestDTO));
    }

    @Test
    void cancelAppointment_Success() {
        setupSecurityContext("test@gmail.com");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(mockAppointment));
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        
        AppointmentResponseDTO cancelledResponse = new AppointmentResponseDTO();
        cancelledResponse.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentMapper.toResponseDTO(any(Appointment.class))).thenReturn(cancelledResponse);

        AppointmentResponseDTO result = appointmentService.cancelAppointment(1L);

        assertNotNull(result);
        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
        assertEquals(AppointmentStatus.CANCELLED, mockAppointment.getStatus());
    }

    @Test
    void cancelAppointment_UnauthorizedUser_ThrowsException() {
        setupSecurityContext("hacker@gmail.com");

        User hacker = User.builder().email("hacker@gmail.com").role(Role.PATIENT).build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(mockAppointment));
        when(userRepository.findByEmail("hacker@gmail.com")).thenReturn(Optional.of(hacker));

        assertThrows(ValidationException.class, () -> appointmentService.cancelAppointment(1L));
    }

    @Test
    void rescheduleAppointment_Success() {
        setupSecurityContext("test@gmail.com");
        
        RescheduleAppointmentDTO rescheduleDTO = new RescheduleAppointmentDTO();
        rescheduleDTO.setNewScheduledDate(LocalDateTime.now().plusDays(2));

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(mockAppointment));
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
        when(appointmentRepository.findByDoctorDoctorIdAndScheduledDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        
        AppointmentResponseDTO rescheduledResponse = new AppointmentResponseDTO();
        rescheduledResponse.setStatus(AppointmentStatus.RESCHEDULED);
        when(appointmentMapper.toResponseDTO(any(Appointment.class))).thenReturn(rescheduledResponse);

        AppointmentResponseDTO result = appointmentService.rescheduleAppointment(1L, rescheduleDTO);

        assertNotNull(result);
        assertEquals(AppointmentStatus.RESCHEDULED, result.getStatus());
        assertEquals(AppointmentStatus.RESCHEDULED, mockAppointment.getStatus());
    }
}
