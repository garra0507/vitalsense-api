package com.biotech.vitalsenseapi.reminder.mapper;

import com.biotech.vitalsenseapi.reminder.dto.ReminderResponseDTO;
import com.biotech.vitalsenseapi.reminder.model.Reminder;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper {

    public ReminderResponseDTO toResponseDTO(Reminder reminder) {
        if (reminder == null) {
            return null;
        }
        return ReminderResponseDTO.builder()
                .reminderId(reminder.getReminderId())
                .patientId(reminder.getPatient().getPatientId())
                .medicationName(reminder.getMedicationName())
                .purpose(reminder.getPurpose())
                .frequency(reminder.getFrequency())
                .reminderTime(reminder.getReminderTime())
                .active(reminder.getActive())
                .createdAt(reminder.getCreatedAt())
                .build();
    }
}
