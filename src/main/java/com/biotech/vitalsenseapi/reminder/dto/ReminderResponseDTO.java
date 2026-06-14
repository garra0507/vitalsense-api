package com.biotech.vitalsenseapi.reminder.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ReminderResponseDTO {

    private Long reminderId;

    private Long patientId;

    private String medicationName;

    private String purpose;

    private String frequency;

    private LocalTime reminderTime;

    private Boolean active;

    private LocalDateTime createdAt;
}