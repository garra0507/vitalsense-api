package com.biotech.vitalsenseapi.patient.mapper;

import com.biotech.vitalsenseapi.patient.dto.PatientResponse;
import com.biotech.vitalsenseapi.patient.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .fullName(patient.getUser().getFirstName() + " " + patient.getUser().getLastName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .emergencyContact(patient.getEmergencyContact())
                .build();
    }
}
