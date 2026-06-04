package com.biotech.vitalsenseapi.prescription.service;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.prescription.dto.PrescriptionRequestDTO;
import com.biotech.vitalsenseapi.prescription.dto.PrescriptionResponseDTO;
import com.biotech.vitalsenseapi.prescription.model.Prescription;
import com.biotech.vitalsenseapi.prescription.repository.PrescriptionRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public PrescriptionResponseDTO createPrescription(
            PrescriptionRequestDTO request
    ) {

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: "
                                        + request.getAppointmentId()
                        )
                );

        if (
                appointment.getStatus()
                        != AppointmentStatus.COMPLETED
        ) {

            throw new ValidationException(
                    "Only completed appointments can generate prescriptions."
            );
        }

        Prescription prescription =
                Prescription.builder()
                        .appointment(appointment)
                        .medications(request.getMedications())
                        .instructions(request.getInstructions())
                        .createdAt(LocalDateTime.now())
                        .build();

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return mapToResponse(savedPrescription);
    }

    private PrescriptionResponseDTO mapToResponse(
            Prescription prescription
    ) {

        return PrescriptionResponseDTO.builder()
                .prescriptionId(
                        prescription.getPrescriptionId()
                )
                .appointmentId(
                        prescription.getAppointment()
                                .getAppointmentId()
                )
                .medications(
                        prescription.getMedications()
                )
                .instructions(
                        prescription.getInstructions()
                )
                .createdAt(
                        prescription.getCreatedAt()
                )
                .build();
    }
}