package com.biotech.vitalsenseapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AvailabilityResponse {

    private Long availabilityId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean available;
}