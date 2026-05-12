package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.LoginRequest;
import com.biotech.vitalsenseapi.dto.RegisterRequest;
import com.biotech.vitalsenseapi.model.User;
import com.biotech.vitalsenseapi.repository.UserRepository;
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
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("test@vitalsense.com");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@vitalsense.com");
        loginRequest.setPassword("password123");

        user = User.builder()
                .email("test@vitalsense.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    void register_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        String result = authService.register(registerRequest);
        
        assertEquals("Usuario registrado", result);
        verify(userRepository, times(1)).save(any(User.class));
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
