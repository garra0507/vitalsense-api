package com.biotech.vitalsenseapi.appointment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime scheduledDate;
}
