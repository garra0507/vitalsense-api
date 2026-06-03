package com.biotech.vitalsenseapi.patient.model;

import com.biotech.vitalsenseapi.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private Integer age;

    private String gender;

    private String emergencyContact;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}