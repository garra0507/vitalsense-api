package com.biotech.vitalsenseapi.assistant.repository;

import com.biotech.vitalsenseapi.assistant.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSessionChatSessionIdOrderByTimestampAsc(Long chatSessionId);
}
