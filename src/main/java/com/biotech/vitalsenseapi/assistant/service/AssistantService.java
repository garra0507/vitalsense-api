package com.biotech.vitalsenseapi.assistant.service;

import com.biotech.vitalsenseapi.appointment.service.AppointmentService;
import com.biotech.vitalsenseapi.assistant.dto.ChatRequest;
import com.biotech.vitalsenseapi.assistant.dto.ChatResponse;
import com.biotech.vitalsenseapi.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public ChatResponse processChat(ChatRequest request) {
        // TODO: Integrate with Spring AI / OpenAI once API Key is available
        // For now, this is a placeholder response
        String userMessage = request.getMessage();
        
        return ChatResponse.builder()
                .response("I received your message: '" + userMessage + "'. AI scheduling is being implemented.")
                .build();
    }
}
