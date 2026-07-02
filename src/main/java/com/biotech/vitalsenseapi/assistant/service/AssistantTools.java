package com.biotech.vitalsenseapi.assistant.service;

import com.biotech.vitalsenseapi.appointment.dto.AppointmentRequestDTO;
import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.service.AppointmentService;
import com.biotech.vitalsenseapi.appointment.service.AvailabilityService;
import com.biotech.vitalsenseapi.doctor.dto.DoctorResponse;
import com.biotech.vitalsenseapi.doctor.model.Specialty;
import com.biotech.vitalsenseapi.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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
    @Tool(description = "Search for available doctors by their specialty (e.g., CARDIOLOGY, PEDIATRICS, DERMATOLOGY, GENERAL_MEDICINE, GYNECOLOGY).")
    public List<DoctorResponse> searchDoctors(
            @ToolParam(description = "The medical specialty to search for") Specialty specialty) {
        return doctorService.searchDoctors(specialty);
    }

    /**
     * Get the available time slots for a specific doctor.
     */
    @Tool(description = "Get the list of available appointment time slots for a specific doctor by their ID.")
    public List<AvailabilityResponse> getDoctorAvailability(
            @ToolParam(description = "The database ID of the doctor") Long doctorId) {
        return availabilityService.getDoctorAvailability(doctorId);
    }

    /**
     * Schedule a new appointment.
     */
    @Tool(description = "Schedule a new appointment for a patient with a specific doctor at a chosen date and time.")
    public AppointmentResponseDTO createAppointment(
            @ToolParam(description = "The ID of the patient booking the appointment") Long patientId,
            @ToolParam(description = "The ID of the doctor to book the appointment with") Long doctorId,
            @ToolParam(description = "The date and time scheduled for the appointment (e.g., '2026-07-05T10:00:00')") LocalDateTime scheduledDate) {
        AppointmentRequestDTO request = new AppointmentRequestDTO();
        request.setPatientId(patientId);
        request.setDoctorId(doctorId);
        request.setScheduledDate(scheduledDate);
        return appointmentService.scheduleAppointment(request);
    }
}
