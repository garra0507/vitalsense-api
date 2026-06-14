package com.biotech.vitalsenseapi.reminder.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ReminderRequestDTO {

    private Long patientId;

    private String medicationName;

    private String purpose;

    private String frequency;

    private LocalTime reminderTime;
}