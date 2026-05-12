package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.LoginRequest;
import com.biotech.vitalsenseapi.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.model.User;
import com.biotech.vitalsenseapi.model.Patient;
import com.biotech.vitalsenseapi.model.Doctor;
import com.biotech.vitalsenseapi.repository.UserRepository;
import com.biotech.vitalsenseapi.repository.PatientRepository;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private PatientRegisterRequest patientRequest;
    private DoctorRegisterRequest doctorRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        patientRequest = new PatientRegisterRequest();
        patientRequest.setUsername("patientuser");
        patientRequest.setPassword("password123");
        patientRequest.setEmail("patient@vitalsense.com");
        patientRequest.setAge(25);

        doctorRequest = new DoctorRegisterRequest();
        doctorRequest.setUsername("doctoruser");
        doctorRequest.setPassword("password123");
        doctorRequest.setEmail("doctor@vitalsense.com");
        doctorRequest.setSpecialty("Cardiology");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@vitalsense.com");
        loginRequest.setPassword("password123");

        user = User.builder()
                .userId(1L)
                .email("test@vitalsense.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    void registerPatient_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        String result = authService.registerPatient(patientRequest);
        
        assertEquals("Paciente registrado con éxito", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void registerDoctor_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = authService.registerDoctor(doctorRequest);

        assertEquals("Doctor registrado con éxito", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("mock-token");

        String token = authService.login(loginRequest);

        assertNotNull(token);
        assertEquals("mock-token", token);
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
