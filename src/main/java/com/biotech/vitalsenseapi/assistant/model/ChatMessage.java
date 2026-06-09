package com.biotech.vitalsenseapi.assistant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String role; // e.g., "USER", "ASSISTANT", "SYSTEM"

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
