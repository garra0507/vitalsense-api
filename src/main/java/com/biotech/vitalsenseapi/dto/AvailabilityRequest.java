package com.biotech.vitalsenseapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailabilityRequest {

    private Long doctorId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}