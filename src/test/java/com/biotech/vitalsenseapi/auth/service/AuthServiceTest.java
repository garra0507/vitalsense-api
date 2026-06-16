package com.biotech.vitalsenseapi.auth.service;

import com.biotech.vitalsenseapi.auth.dto.LoginRequest;
import com.biotech.vitalsenseapi.auth.dto.SocialLoginRequest;
import com.biotech.vitalsenseapi.auth.model.Role;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import com.biotech.vitalsenseapi.doctor.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.model.Specialty;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.patient.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.shared.exception.AuthenticationException;
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
import static org.mockito.ArgumentMatchers.anyString;
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

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .userId(1L)
                .email("test@gmail.com")
                .password("encoded_password")
                .role(Role.PATIENT)
                .build();
    }

    @Test
    void registerPatient_Success() {
        // Arrange
        PatientRegisterRequest request = new PatientRegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        String result = authService.registerPatient(request);

        // Assert
        assertEquals("Paciente registrado con éxito", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void registerDoctor_Success() {
        // Arrange
        DoctorRegisterRequest request = new DoctorRegisterRequest();
        request.setEmail("doctor@gmail.com");
        request.setPassword("password");
        request.setSpecialty(Specialty.CARDIOLOGIA);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        String result = authService.registerDoctor(request);

        // Assert
        assertEquals("Doctor registrado con éxito", result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void login_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);
        when(jwtService.generateToken("test@gmail.com")).thenReturn("mock_jwt_token");

        // Act
        String token = authService.login(request);

        // Assert
        assertEquals("mock_jwt_token", token);
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authService.login(request));
    }

    @Test
    void login_WrongPassword_ThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("wrong_password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authService.login(request));
    }

    @Test
    void socialLogin_ExistingUser_Success() {
        // Arrange
        SocialLoginRequest request = new SocialLoginRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken("test@gmail.com")).thenReturn("mock_jwt_token");

        // Act
        String token = authService.socialLogin(request);

        // Assert
        assertEquals("mock_jwt_token", token);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void socialLogin_NewUser_CreatesProfileAndReturnsToken() {
        // Arrange
        SocialLoginRequest request = new SocialLoginRequest();
        request.setEmail("new@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtService.generateToken("test@gmail.com")).thenReturn("mock_jwt_token"); // mockUser has test@gmail.com

        // Act
        String token = authService.socialLogin(request);

        // Assert
        assertEquals("mock_jwt_token", token);
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}
