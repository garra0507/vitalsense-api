package com.biotech.vitalsenseapi.appointment.dto;

import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarAppointmentDTO {

    private Long appointmentId;

    private String patientName;

    private String doctorName;

    private LocalDateTime scheduledDate;

    private AppointmentStatus status;
}