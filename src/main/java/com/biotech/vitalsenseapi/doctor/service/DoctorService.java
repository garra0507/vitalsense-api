package com.biotech.vitalsenseapi.doctor.service;

import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponse> searchDoctors(
            String specialty
    ) {

        List<Doctor> doctors =
                doctorRepository
                        .findBySpecialtyContainingIgnoreCase(
                                specialty
                        );

        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DoctorResponse mapToResponse(
            Doctor doctor
    ) {

        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .fullName(
                        doctor.getUser().getFirstName()
                                + " "
                                + doctor.getUser().getLastName()
                )
                .specialty(doctor.getSpecialty())
                .yearsOfExperience(
                        doctor.getYearsOfExperience()
                )
                .consultationFee(
                        doctor.getConsultationFee()
                )
                .biography(doctor.getBiography())
                .build();
    }
}