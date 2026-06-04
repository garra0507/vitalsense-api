package com.biotech.vitalsenseapi.patient.service;

import com.biotech.vitalsenseapi.patient.dto.PatientRegisterRequest;
import com.biotech.vitalsenseapi.patient.dto.PatientResponse;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public PatientResponse createPatient(
            PatientRegisterRequest request
    ) {

        Patient patient = Patient.builder()
                .age(request.getAge())
                .gender(request.getGender())
                .emergencyContact(request.getEmergencyContact())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    private PatientResponse mapToResponse(
            Patient patient
    ) {

        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .fullName("Patient Test")
                .age(patient.getAge())
                .gender(patient.getGender())
                .emergencyContact(patient.getEmergencyContact())
                .build();
    }
}