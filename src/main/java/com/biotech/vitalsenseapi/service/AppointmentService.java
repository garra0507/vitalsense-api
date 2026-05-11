package com.biotech.vitalsenseapi.service;

import com.biotech.vitalsenseapi.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.exception.AppointmentConflictException;
import com.biotech.vitalsenseapi.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.exception.ValidationException;
import com.biotech.vitalsenseapi.model.*;
import com.biotech.vitalsenseapi.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.repository.DoctorRepository;
import com.biotech.vitalsenseapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public AppointmentResponseDTO scheduleAppointment(AppointmentRequestDTO request) {
        // 1. Validation: Time must be in the future
        if (request.getScheduledDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Scheduled date must be in the future.");
        }

        // 2. Integrity: Patient and Doctor must exist
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        // 3. Conflict Validation: Double Booking
        appointmentRepository.findByDoctorDoctorIdAndScheduledDate(request.getDoctorId(), request.getScheduledDate())
                .ifPresent(a -> {
                    throw new AppointmentConflictException("Doctor already has an appointment at " + request.getScheduledDate());
                });

        // 4. Create Appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .scheduledDate(request.getScheduledDate())
                .status(AppointmentStatus.CONFIRMED)
                .meetLink(generateMeetLink())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    private String generateMeetLink() {
        return "https://meet.vitalsense.com/" + UUID.randomUUID().toString().substring(0, 8);
    }

    private AppointmentResponseDTO mapToResponse(Appointment appointment) {
        AppointmentResponseDTO response = new AppointmentResponseDTO();
        response.setAppointmentId(appointment.getAppointmentId());
        response.setPatientId(appointment.getPatient().getPatientId());
        response.setDoctorId(appointment.getDoctor().getDoctorId());
        response.setScheduledDate(appointment.getScheduledDate());
        response.setStatus(appointment.getStatus());
        response.setMeetLink(appointment.getMeetLink());
        return response;
    }
}
