package com.biotech.vitalsenseapi.appointment.mapper;

import com.biotech.vitalsenseapi.appointment.dto.AvailabilityResponse;
import com.biotech.vitalsenseapi.appointment.model.Availability;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityMapper {

    public AvailabilityResponse toResponseDTO(Availability availability) {
        if (availability == null) {
            return null;
        }
        return AvailabilityResponse.builder()
                .availabilityId(availability.getAvailabilityId())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .available(availability.getAvailable())
                .build();
    }
}
