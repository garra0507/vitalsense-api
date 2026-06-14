package com.biotech.vitalsenseapi.auth.service;

import com.biotech.vitalsenseapi.auth.dto.LoginRequest;
import com.biotech.vitalsenseapi.auth.dto.SocialLoginRequest;
import com.biotech.vitalsenseapi.auth.dto.UserResponse;
import com.biotech.vitalsenseapi.auth.mapper.AuthMapper;
import com.biotech.vitalsenseapi.auth.model.Role;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import com.biotech.vitalsenseapi.doctor.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.patient.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.shared.exception.AuthenticationException;
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
    private final AuthMapper authMapper;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public String registerPatient(PatientRegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.PATIENT)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        Patient patient = Patient.builder()
                .user(savedUser)
                .age(request.getAge())
                .gender(request.getGender())
                .emergencyContact(request.getEmergencyContact())
                .build();
        
        patientRepository.save(patient);

        return "Paciente registrado con éxito";
    }

    @Transactional
    public String registerDoctor(DoctorRegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.DOCTOR)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(savedUser)
                .specialty(request.getSpecialty())
                .yearsOfExperience(request.getYearsOfExperience())
                .consultationFee(request.getConsultationFee())
                .biography(request.getBiography())
                .build();

        doctorRepository.save(doctor);

        return "Doctor registrado con éxito";
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Contraseña incorrecta");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public UserResponse getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));

        Long profileId = null;
        if (user.getRole() == Role.PATIENT) {
            profileId = patientRepository.findByUserUserId(user.getUserId())
                    .map(Patient::getPatientId)
                    .orElse(null);
        } else if (user.getRole() == Role.DOCTOR) {
            profileId = doctorRepository.findByUserUserId(user.getUserId())
                    .map(Doctor::getDoctorId)
                    .orElse(null);
        }

        return authMapper.toUserResponse(user, profileId);
    }

    @Transactional
    public String socialLogin(SocialLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(request.getEmail())
                            .username(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .password(passwordEncoder.encode("SOCIAL_LOGIN"))
                            .role(Role.PATIENT)
                            .active(true)
                            .build();
                    User saved = userRepository.save(newUser);
                    
                    // Auto-create patient profile for social login
                    Patient patient = Patient.builder()
                            .user(saved)
                            .build();
                    patientRepository.save(patient);
                    
                    return saved;
                });

        return jwtService.generateToken(user.getEmail());
    }
}
