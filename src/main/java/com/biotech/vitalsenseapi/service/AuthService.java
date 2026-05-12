package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.LoginRequest;
import com.biotech.vitalsenseapi.dto.RegisterRequest;
import com.biotech.vitalsenseapi.dto.UserResponse;
import com.biotech.vitalsenseapi.model.Doctor;
import com.biotech.vitalsenseapi.model.Patient;
import com.biotech.vitalsenseapi.model.User;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import com.biotech.vitalsenseapi.repository.PatientRepository;
import com.biotech.vitalsenseapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Transactional
    public String register(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // Auto-create profile based on role
        if ("PATIENT".equalsIgnoreCase(savedUser.getRole())) {
            Patient patient = Patient.builder()
                    .user(savedUser)
                    .build();
            patientRepository.save(patient);
        } else if ("DOCTOR".equalsIgnoreCase(savedUser.getRole())) {
            Doctor doctor = Doctor.builder()
                    .user(savedUser)
                    .build();
            doctorRepository.save(doctor);
        }

        return "Usuario registrado";
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow(
                () -> new RuntimeException("Usuario no encontrado")
        );

        boolean validPassword = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!validPassword) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public UserResponse getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long profileId = null;
        if ("PATIENT".equalsIgnoreCase(user.getRole())) {
            profileId = patientRepository.findByUserUserId(user.getUserId())
                    .map(Patient::getPatientId)
                    .orElse(null);
        } else if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
            profileId = doctorRepository.findByUserUserId(user.getUserId())
                    .map(Doctor::getDoctorId)
                    .orElse(null);
        }

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .profileId(profileId)
                .build();
    }
}