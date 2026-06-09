package com.biotech.vitalsenseapi.assistant.service;

import com.biotech.vitalsenseapi.assistant.dto.ChatRequest;
import com.biotech.vitalsenseapi.assistant.dto.ChatResponse;
import com.biotech.vitalsenseapi.assistant.mapper.AssistantMapper;
import com.biotech.vitalsenseapi.assistant.model.ChatMessage;
import com.biotech.vitalsenseapi.assistant.model.ChatSession;
import com.biotech.vitalsenseapi.assistant.repository.ChatMessageRepository;
import com.biotech.vitalsenseapi.assistant.repository.ChatSessionRepository;
import com.biotech.vitalsenseapi.auth.model.User;
import com.biotech.vitalsenseapi.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final AssistantMapper assistantMapper;
    private final AssistantTools assistantTools;

    @Transactional
    public ChatResponse processChat(ChatRequest request) {
        // 1. Get Current User
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Get or Create Chat Session
        ChatSession session = chatSessionRepository.findByUserUserId(user.getUserId())
                .orElseGet(() -> {
                    ChatSession newSession = ChatSession.builder()
                            .user(user)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return chatSessionRepository.save(newSession);
                });

        // 3. Save User Message
        ChatMessage userMessage = ChatMessage.builder()
                .chatSession(session)
                .content(request.getMessage())
                .role("USER")
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(userMessage);

        // 4. Logic Orchestration (Placeholder for Spring AI)
        // Here we would normally call the LLM and pass the tools
        String aiResponseContent = generatePlaceholderResponse(request.getMessage());

        // 5. Save Assistant Response
        ChatMessage assistantMessage = ChatMessage.builder()
                .chatSession(session)
                .content(aiResponseContent)
                .role("ASSISTANT")
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(assistantMessage);

        return assistantMapper.toChatResponse(assistantMessage);
    }

    private String generatePlaceholderResponse(String userMessage) {
        // Simple mock logic for demonstration
        if (userMessage.toLowerCase().contains("doctor") || userMessage.toLowerCase().contains("especialidad")) {
            return "Puedo ayudarte a buscar un doctor. ¿Qué especialidad necesitas?";
        } else if (userMessage.toLowerCase().contains("cita") || userMessage.toLowerCase().contains("reserva")) {
            return "Claro, puedo ayudarte a programar una cita. ¿Con qué doctor te gustaría atenderte?";
        }
        return "Hola, soy tu asistente de VitalSense. ¿En qué puedo ayudarte hoy? Puedo buscar doctores, revisar disponibilidad o agendar citas por ti.";
    }
}
