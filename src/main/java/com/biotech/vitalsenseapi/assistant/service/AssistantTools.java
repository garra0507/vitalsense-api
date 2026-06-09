package com.biotech.vitalsenseapi.assistant.service;

import com.biotech.vitalsenseapi.appointment.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.service.AppointmentService;
import com.biotech.vitalsenseapi.appointment.service.AvailabilityService;
import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssistantTools {

    private final DoctorService doctorService;
    private final AvailabilityService availabilityService;
    private final AppointmentService appointmentService;

    /**
     * Search for doctors by their specialty.
     */
    public List<DoctorResponse> searchDoctors(String specialty) {
        return doctorService.searchDoctors(specialty);
    }

    /**
     * Get the available time slots for a specific doctor.
     */
    public List<AvailabilityResponse> getDoctorAvailability(Long doctorId) {
        return availabilityService.getDoctorAvailability(doctorId);
    }

    /**
     * Schedule a new appointment.
     */
    public AppointmentResponseDTO createAppointment(Long patientId, Long doctorId, LocalDateTime scheduledDate) {
        AppointmentRequestDTO request = new AppointmentRequestDTO();
        request.setPatientId(patientId);
        request.setDoctorId(doctorId);
        request.setScheduledDate(scheduledDate);
        return appointmentService.scheduleAppointment(request);
    }
}
