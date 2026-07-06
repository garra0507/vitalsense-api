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
import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AssistantMapper assistantMapper;
    private final ChatClient assistantChatClient;

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

        // 3. Save User Message immediately in its own transaction
        saveMessage(session, request.getMessage(), "USER");

        // 4. Fetch Patient details to supply to system instructions
        Patient patient = patientRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // 5. Fetch entire sorted chat history (includes user's current message saved above)
        List<ChatMessage> history = chatMessageRepository.findByChatSessionChatSessionIdOrderByTimestampAsc(session.getChatSessionId());
        List<Message> springAiMessages = new ArrayList<>();
        for (ChatMessage msg : history) {
            if ("USER".equalsIgnoreCase(msg.getRole())) {
                springAiMessages.add(new UserMessage(msg.getContent()));
            } else if ("ASSISTANT".equalsIgnoreCase(msg.getRole())) {
                springAiMessages.add(new AssistantMessage(msg.getContent()));
            }
        }

        // 6. Orchestrate prompt with current patient context
        String systemInstructionOverride = String.format(
                "You are the VitalSense Virtual Assistant. The current patient is: %s %s. Patient ID is %d (NEVER reveal this numeric ID to the user).",
                user.getFirstName(), user.getLastName(), patient.getPatientId()
        );

        // 7. Call LLM — wrapped in try/catch so tool errors never rollback saved messages
        String aiResponseContent;
        try {
            aiResponseContent = assistantChatClient.prompt()
                    .system(systemInstructionOverride)
                    .messages(springAiMessages)
                    .call()
                    .content();
            if (aiResponseContent == null) aiResponseContent = "Lo siento, no pude procesar tu solicitud en este momento. Por favor intenta de nuevo.";
        } catch (Exception e) {
            aiResponseContent = "Lo siento, ocurrió un error al procesar tu solicitud. Por favor intenta de nuevo.";
        }

        // 8. Save Assistant Response immediately in its own transaction
        ChatMessage assistantMessage = saveMessage(session, aiResponseContent, "ASSISTANT");

        return assistantMapper.toChatResponse(assistantMessage);
    }

    /**
     * Persists a chat message in its own independent transaction.
     * Using REQUIRES_NEW ensures the write is committed immediately and
     * is never rolled back by any exception in the calling method.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChatMessage saveMessage(ChatSession session, String content, String role) {
        ChatMessage message = ChatMessage.builder()
                .chatSession(session)
                .content(content)
                .role(role)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(message);
    }
}
