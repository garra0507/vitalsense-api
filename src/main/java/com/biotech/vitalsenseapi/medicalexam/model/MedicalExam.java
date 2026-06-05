package com.biotech.vitalsenseapi.medicalexam.model;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String examName;

    private String instructions;

    private LocalDateTime createdAt;
}