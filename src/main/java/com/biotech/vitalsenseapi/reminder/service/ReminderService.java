package com.biotech.vitalsenseapi.reminder.service;

import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.reminder.dto.ReminderRequestDTO;
import com.biotech.vitalsenseapi.reminder.dto.ReminderResponseDTO;
import com.biotech.vitalsenseapi.reminder.mapper.ReminderMapper;
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
    private final ReminderMapper reminderMapper;

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

        return reminderMapper.toResponseDTO(savedReminder);
    }

    public List<ReminderResponseDTO>
    getRemindersByPatient(Long patientId) {

        return reminderRepository
                .findByPatientPatientId(patientId)
                .stream()
                .map(reminderMapper::toResponseDTO)
                .toList();
    }
}
