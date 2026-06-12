package com.biotech.vitalsenseapi.appointment.service;

import com.biotech.vitalsenseapi.appointment.dto.RescheduleAppointmentDTO;
import com.biotech.vitalsenseapi.appointment.mapper.AppointmentMapper;
import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.exception.AppointmentConflictException;
import com.biotech.vitalsenseapi.auth.model.Role;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import com.biotech.vitalsenseapi.doctor.model.Doctor;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.doctor.repository.DoctorRepository;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.biotech.vitalsenseapi.appointment.model.AppointmentPaymentStatus;
import com.biotech.vitalsenseapi.appointment.dto.CalendarAppointmentDTO;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

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

        // Security: Ensure patient is booking for themselves
        checkOwnership(patient.getUser());

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
                .paymentAmount(request.getPaymentAmount())
                .status(AppointmentStatus.PENDING)
                .paymentStatus(AppointmentPaymentStatus.PENDING)
                .meetLink(generateMeetLink())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDTO(savedAppointment);
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        
        // Security: Check if user is either the patient or the doctor of the appointment
        checkAppointmentAccess(appointment);

        return appointmentMapper.toResponseDTO(appointment);
    }

    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        
        checkOwnership(patient.getUser());

        return appointmentRepository.findByPatientPatientId(patientId).stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        checkOwnership(doctor.getUser());

        return appointmentRepository.findByDoctorDoctorId(doctorId).stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String generateMeetLink() {
        return "https://meet.vitalsense.com/" + UUID.randomUUID().toString();
    }

    @Transactional
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        checkAppointmentAccess(appointment);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ValidationException("Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ValidationException("Completed appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDTO(updatedAppointment);
    }

    @Transactional
    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, RescheduleAppointmentDTO request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        checkAppointmentAccess(appointment);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ValidationException("Cancelled appointments cannot be rescheduled.");
        }

        if (request.getNewScheduledDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("New scheduled date must be in the future.");
        }

        appointmentRepository.findByDoctorDoctorIdAndScheduledDate(appointment.getDoctor().getDoctorId(), request.getNewScheduledDate())
                .ifPresent(a -> {
                    if (!a.getAppointmentId().equals(appointmentId)) {
                        throw new AppointmentConflictException("Doctor already has an appointment at this time.");
                    }
                });

        appointment.setScheduledDate(request.getNewScheduledDate());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDTO(updatedAppointment);
    }

    public List<CalendarAppointmentDTO> getAppointmentsCalendarByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        checkOwnership(doctor.getUser());

        List<Appointment> appointments = appointmentRepository.findByDoctorDoctorIdOrderByScheduledDateAsc(doctorId);
        return appointments.stream()
                .map(appointmentMapper::toCalendarDTO)
                .toList();
    }

    private void checkOwnership(User owner) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        
        if (!currentUser.getEmail().equals(owner.getEmail()) && currentUser.getRole() != Role.ADMIN) {
            throw new ValidationException("You do not have permission to access this resource.");
        }
    }

    private void checkAppointmentAccess(Appointment appointment) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        boolean isPatient = currentUser.getEmail().equals(appointment.getPatient().getUser().getEmail());
        boolean isDoctor = currentUser.getEmail().equals(appointment.getDoctor().getUser().getEmail());

        if (!isPatient && !isDoctor && currentUser.getRole() != Role.ADMIN) {
            throw new ValidationException("You do not have permission to access this appointment.");
        }
    }
}
