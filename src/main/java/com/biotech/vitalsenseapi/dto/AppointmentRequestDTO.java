package com.biotech.vitalsenseapi.dto;

import com.biotech.vitalsenseapi.model.AppointmentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime scheduledDate;
    private AppointmentType type;
}
