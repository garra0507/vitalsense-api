package com.biotech.vitalsenseapi.auth.service;

import com.biotech.vitalsenseapi.auth.dto.LoginRequest;
import com.biotech.vitalsenseapi.auth.dto.RegisterRequest;
import com.biotech.vitalsenseapi.auth.dto.SocialLoginRequest;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

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

        userRepository.save(user);

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
    public String socialLogin(
            SocialLoginRequest request
    ) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {

            user = User.builder()
                    .email(request.getEmail())
                    .username(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(
                            passwordEncoder.encode("SOCIAL_LOGIN")
                    )
                    .role("PATIENT")
                    .active(true)
                    .build();

            userRepository.save(user);
        }

        return jwtService.generateToken(
                user.getEmail()
        );
    }
}