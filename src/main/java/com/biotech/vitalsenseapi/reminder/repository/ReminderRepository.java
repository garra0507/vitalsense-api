package com.biotech.vitalsenseapi.reminder.repository;

import com.biotech.vitalsenseapi.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository
        extends JpaRepository<Reminder, Long> {

    List<Reminder>
    findByPatientPatientId(Long patientId);
}