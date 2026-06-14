package com.biotech.vitalsenseapi.appointment.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchAvailabilityRequest {
    private Long doctorId;
    private List<TimeSlotRequest> slots;
}
