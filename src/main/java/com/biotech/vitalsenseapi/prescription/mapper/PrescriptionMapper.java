package com.biotech.vitalsenseapi.prescription.mapper;

import com.biotech.vitalsenseapi.prescription.dto.PrescriptionResponseDTO;
import com.biotech.vitalsenseapi.prescription.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public PrescriptionResponseDTO toResponseDTO(Prescription prescription) {
        if (prescription == null) {
            return null;
        }
        return PrescriptionResponseDTO.builder()
                .prescriptionId(prescription.getPrescriptionId())
                .appointmentId(prescription.getAppointment().getAppointmentId())
                .medications(prescription.getMedications())
                .instructions(prescription.getInstructions())
                .createdAt(prescription.getCreatedAt())
                .build();
    }
}
