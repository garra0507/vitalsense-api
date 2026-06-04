package com.biotech.vitalsenseapi.appointment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleAppointmentDTO {

    private LocalDateTime newScheduledDate;
}