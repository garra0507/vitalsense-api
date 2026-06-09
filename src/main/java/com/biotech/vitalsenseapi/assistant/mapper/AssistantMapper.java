package com.biotech.vitalsenseapi.assistant.mapper;

import com.biotech.vitalsenseapi.assistant.dto.ChatResponse;
import com.biotech.vitalsenseapi.assistant.model.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class AssistantMapper {

    public ChatResponse toChatResponse(ChatMessage message) {
        return ChatResponse.builder()
                .response(message.getContent())
                .build();
    }
}
