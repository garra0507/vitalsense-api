package com.biotech.vitalsenseapi.appointment.dto;

import com.biotech.vitalsenseapi.appointment.model.AppointmentPaymentStatus;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.doctor.model.Specialty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private Specialty doctorSpecialty;
    private LocalDateTime scheduledDate;
    private AppointmentStatus status;
    private AppointmentPaymentStatus paymentStatus;
    private String meetLink;
    private Double paymentAmount;
}
