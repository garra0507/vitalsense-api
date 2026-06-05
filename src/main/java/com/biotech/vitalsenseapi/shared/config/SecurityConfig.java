package com.biotech.vitalsenseapi.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/medical-exams/**",
                                "/api/reminders/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/api/doctors/search",
                                "/api/appointments/calendar/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/api/medical-exams/**"
                        )
                        .hasAnyRole("DOCTOR", "PATIENT")

                        .requestMatchers(
                                "/api/payments/**"
                        )
                        .hasRole("PATIENT")

                        .requestMatchers(
                                "/api/prescriptions/**"
                        )
                        .hasRole("DOCTOR")

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                .formLogin(form -> form.disable());

        return http.build();
    }
}