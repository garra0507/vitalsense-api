package com.biotech.vitalsenseapi.doctor.service;

import com.biotech.vitalsenseapi.doctor.dto.DoctorRegisterRequest;
import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

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
                .fullName("Doctor Test")
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
    @Transactional
    public DoctorResponse createDoctor(
            DoctorRegisterRequest request
    ) {

        Doctor doctor = Doctor.builder()
                .specialty(request.getSpecialty())
                .yearsOfExperience(request.getYearsOfExperience())
                .consultationFee(request.getConsultationFee())
                .biography(request.getBiography())
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponse(savedDoctor);
    }
}