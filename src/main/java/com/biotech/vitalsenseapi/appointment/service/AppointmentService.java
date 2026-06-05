package com.biotech.vitalsenseapi.appointment.service;

import com.biotech.vitalsenseapi.appointment.dto.RescheduleAppointmentDTO;
import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.exception.AppointmentConflictException;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.biotech.vitalsenseapi.appointment.model.AppointmentPaymentStatus;
import com.biotech.vitalsenseapi.appointment.dto.CalendarAppointmentDTO;
import java.time.LocalDate;

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
                .status(AppointmentStatus.PENDING)
                .paymentStatus(AppointmentPaymentStatus.PENDING)
                .meetLink(generateMeetLink())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateMeetLink() {
        return "https://meet.vitalsense.com/" + UUID.randomUUID().toString();
    }

    private AppointmentResponseDTO mapToResponse(Appointment appointment) {
        AppointmentResponseDTO response = new AppointmentResponseDTO();
        response.setAppointmentId(appointment.getAppointmentId());
        response.setPatientId(appointment.getPatient().getPatientId());
        response.setDoctorId(appointment.getDoctor().getDoctorId());
        response.setScheduledDate(appointment.getScheduledDate());
        response.setStatus(appointment.getStatus());
        response.setMeetLink(appointment.getMeetLink());
        response.setPaymentStatus(appointment.getPaymentStatus());
        return response;
    }
    @Transactional
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId
                ));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ValidationException("Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ValidationException("Completed appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    @Transactional
    public AppointmentResponseDTO rescheduleAppointment(
            Long appointmentId,
            RescheduleAppointmentDTO request
    ) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId
                ));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ValidationException(
                    "Cancelled appointments cannot be rescheduled."
            );
        }

        if (request.getNewScheduledDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    "New scheduled date must be in the future."
            );
        }

        appointmentRepository
                .findByDoctorDoctorIdAndScheduledDate(
                        appointment.getDoctor().getDoctorId(),
                        request.getNewScheduledDate()
                )
                .ifPresent(a -> {
                    if (!a.getAppointmentId().equals(appointmentId)) {
                        throw new AppointmentConflictException(
                                "Doctor already has an appointment at this time."
                        );
                    }
                });

        appointment.setScheduledDate(request.getNewScheduledDate());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    public List<CalendarAppointmentDTO>
    getAppointmentsCalendarByDoctor(
            Long doctorId
    ) {

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorDoctorIdOrderByScheduledDateAsc(
                                doctorId
                        );

        return appointments.stream()
                .map(appointment ->
                        CalendarAppointmentDTO.builder()
                                .appointmentId(
                                        appointment.getAppointmentId()
                                )
                                .patientName(
                                        appointment.getPatient()
                                                .getUser()
                                                .getFirstName()
                                                + " "
                                                + appointment.getPatient()
                                                .getUser()
                                                .getLastName()
                                )
                                .doctorName(
                                        appointment.getDoctor()
                                                .getUser()
                                                .getFirstName()
                                                + " "
                                                + appointment.getDoctor()
                                                .getUser()
                                                .getLastName()
                                )
                                .scheduledDate(
                                        appointment.getScheduledDate()
                                )
                                .status(
                                        appointment.getStatus()
                                )
                                .build()
                )
                .toList();
    }
}
