package com.biotech.vitalsenseapi.payment.service;

import com.biotech.vitalsenseapi.appointment.model.Appointment;
import com.biotech.vitalsenseapi.appointment.model.AppointmentPaymentStatus;
import com.biotech.vitalsenseapi.appointment.model.AppointmentStatus;
import com.biotech.vitalsenseapi.appointment.repository.AppointmentRepository;
import com.biotech.vitalsenseapi.payment.dto.PaymentRequestDTO;
import com.biotech.vitalsenseapi.payment.dto.PaymentResponseDTO;
import com.biotech.vitalsenseapi.payment.mapper.PaymentMapper;
import com.biotech.vitalsenseapi.payment.model.Payment;
import com.biotech.vitalsenseapi.payment.repository.PaymentRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import com.biotech.vitalsenseapi.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponseDTO processPayment(
            PaymentRequestDTO request
    ) {

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: "
                                        + request.getAppointmentId()
                        )
                );

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ValidationException(
                    "Cancelled appointments cannot be paid."
            );
        }

        if (appointment.getPaymentStatus()== AppointmentPaymentStatus.PAID) {
            throw new ValidationException(
                    "Appointment is already paid."
            );
        }

        Patient patient = appointment.getPatient();

        if (patient.getBalance() < appointment.getPaymentAmount()) {

            Payment failedPayment = Payment.builder()
                    .appointment(appointment)
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .paymentDate(LocalDateTime.now())
                    .status("PENDING")
                    .build();

            paymentRepository.save(failedPayment);

            throw new ValidationException(
                    "Insufficient balance to pay appointment"
            );
        }

        Payment payment = Payment.builder()
                .appointment(appointment)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentDate(LocalDateTime.now())
                .status("PAID")
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        patient.setBalance(
                patient.getBalance()
                        - appointment.getPaymentAmount()
        );

        appointment.setPaymentStatus(
                AppointmentPaymentStatus.PAID
        );

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        appointmentRepository.save(appointment);

        return paymentMapper.toResponseDTO(savedPayment);
    }
}
