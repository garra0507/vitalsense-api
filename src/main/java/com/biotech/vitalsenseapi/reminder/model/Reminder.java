package com.biotech.vitalsenseapi.reminder.model;

import com.biotech.vitalsenseapi.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String medicationName;

    private String frequency;

    private LocalTime reminderTime;

    private Boolean active;

    private LocalDateTime createdAt;
}