package com.biotech.vitalsenseapi.auth.config;

import com.biotech.vitalsenseapi.auth.model.Role;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@vitalsense.com").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@vitalsense.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("VitalSense")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("DatabaseSeeder: Default admin user created successfully (admin@vitalsense.com / admin123)");
        } else {
            log.info("DatabaseSeeder: Default admin user already exists");
        }
    }
}
