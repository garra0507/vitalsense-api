package com.biotech.vitalsenseapi.appointment.mapper;

import com.biotech.vitalsenseapi.appointment.dto.AppointmentResponseDTO;
import com.biotech.vitalsenseapi.appointment.dto.CalendarAppointmentDTO;
import com.biotech.vitalsenseapi.appointment.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentResponseDTO response = new AppointmentResponseDTO();
        response.setAppointmentId(appointment.getAppointmentId());
        response.setPatientId(appointment.getPatient().getPatientId());
        response.setPatientName(appointment.getPatient().getUser().getFirstName() + " " + appointment.getPatient().getUser().getLastName());
        response.setDoctorId(appointment.getDoctor().getDoctorId());
        response.setDoctorName(appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName());
        response.setDoctorSpecialty(appointment.getDoctor().getSpecialty());
        response.setScheduledDate(appointment.getScheduledDate());
        response.setStatus(appointment.getStatus());
        response.setMeetLink(appointment.getMeetLink());
        response.setPaymentStatus(appointment.getPaymentStatus());
        response.setPaymentAmount(appointment.getPaymentAmount());
        return response;
    }

    public CalendarAppointmentDTO toCalendarDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return CalendarAppointmentDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientName(appointment.getPatient().getUser().getFirstName() + " " + appointment.getPatient().getUser().getLastName())
                .doctorName(appointment.getDoctor().getUser().getFirstName() + " " + appointment.getDoctor().getUser().getLastName())
                .scheduledDate(appointment.getScheduledDate())
                .status(appointment.getStatus())
                .build();
    }
}
