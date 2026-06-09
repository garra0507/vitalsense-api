package com.biotech.vitalsenseapi.assistant.repository;

import com.biotech.vitalsenseapi.assistant.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
