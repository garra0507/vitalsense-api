package com.biotech.vitalsenseapi.dto;

import com.biotech.vitalsenseapi.model.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime scheduledDate;
    private AppointmentStatus status;
    private String meetLink;
}
