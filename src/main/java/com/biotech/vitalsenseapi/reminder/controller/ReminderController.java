package com.biotech.vitalsenseapi.reminder.controller;

import com.biotech.vitalsenseapi.reminder.dto.ReminderRequestDTO;
import com.biotech.vitalsenseapi.reminder.dto.ReminderResponseDTO;
import com.biotech.vitalsenseapi.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<ReminderResponseDTO>
    createReminder(
            @RequestBody ReminderRequestDTO request
    ) {

        return new ResponseEntity<>(
                reminderService.createReminder(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ReminderResponseDTO>>
    getRemindersByPatient(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                reminderService
                        .getRemindersByPatient(patientId)
        );
    }

    @PatchMapping("/{reminderId}/toggle")
    public ResponseEntity<ReminderResponseDTO> toggleReminder(@PathVariable Long reminderId) {
        return ResponseEntity.ok(reminderService.toggleReminder(reminderId));
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long reminderId) {
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.noContent().build();
    }
    }