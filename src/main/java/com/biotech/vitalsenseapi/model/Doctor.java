package com.biotech.vitalsenseapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    private String specialty;

    private Integer yearsOfExperience;

    private Double consultationFee;

    private String biography;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}