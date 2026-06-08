package com.biotech.vitalsenseapi.doctor.mapper;

import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponse toResponse(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .fullName(doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName())
                .specialty(doctor.getSpecialty())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .consultationFee(doctor.getConsultationFee())
                .biography(doctor.getBiography())
                .build();
    }
}
