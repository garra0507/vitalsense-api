package com.biotech.vitalsenseapi.reminder.service;

import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.reminder.dto.ReminderRequestDTO;
import com.biotech.vitalsenseapi.reminder.dto.ReminderResponseDTO;
import com.biotech.vitalsenseapi.reminder.model.Reminder;
import com.biotech.vitalsenseapi.reminder.repository.ReminderRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    private final PatientRepository patientRepository;

    public ReminderResponseDTO createReminder(
            ReminderRequestDTO request
    ) {

        Patient patient =
                patientRepository.findById(
                        request.getPatientId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found"
                        )
                );

        Reminder reminder =
                Reminder.builder()
                        .patient(patient)
                        .medicationName(
                                request.getMedicationName()
                        )
                        .frequency(
                                request.getFrequency()
                        )
                        .reminderTime(
                                request.getReminderTime()
                        )
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();

        Reminder savedReminder =
                reminderRepository.save(reminder);

        return mapToResponse(savedReminder);
    }

    public List<ReminderResponseDTO>
    getRemindersByPatient(Long patientId) {

        return reminderRepository
                .findByPatientPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReminderResponseDTO mapToResponse(
            Reminder reminder
    ) {

        return ReminderResponseDTO.builder()
                .reminderId(reminder.getReminderId())
                .patientId(
                        reminder.getPatient()
                                .getPatientId()
                )
                .medicationName(
                        reminder.getMedicationName()
                )
                .frequency(
                        reminder.getFrequency()
                )
                .reminderTime(
                        reminder.getReminderTime()
                )
                .active(reminder.getActive())
                .createdAt(reminder.getCreatedAt())
                .build();
    }
}