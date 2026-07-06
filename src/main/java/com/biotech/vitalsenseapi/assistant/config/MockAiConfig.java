package com.biotech.vitalsenseapi.assistant.config;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("mock-ai")
public class MockAiConfig {

    @Bean
    @Primary
    public ChatModel chatModel() {
        return new ChatModel() {
            @Override
            public ChatResponse call(Prompt prompt) {
                String userMessage = prompt.getInstructions().getLast().getText();
                String reply = "MOCK AI RESPONSE: Recibí tu mensaje. Modo simulación (perfil mock-ai) activo. Tu mensaje: " + userMessage;

                if (userMessage.toLowerCase().contains("cardiolog") || userMessage.toLowerCase().contains("cardiológ")) {
                    reply = "MOCK AI: Encontré al Dr. Juan Pérez (Cardiología). ID de Doctor: 1. ¿Deseas ver su disponibilidad?";
                } else if (userMessage.toLowerCase().contains("disponibilidad") || userMessage.toLowerCase().contains("turnos")) {
                    reply = "MOCK AI: El Dr. Juan Pérez tiene disponibilidad el 2026-07-05 a las 10:00. ¿Deseas reservar la cita?";
                } else if (userMessage.toLowerCase().contains("reservar") || userMessage.toLowerCase().contains("agendar")) {
                    reply = "MOCK AI: ¡Cita programada con éxito! Confirmado para el 2026-07-05 a las 10:00 con el Dr. Juan Pérez.";
                }

                Generation gen = new Generation(new AssistantMessage(reply));
                return new ChatResponse(List.of(gen));
            }
        };
    }

    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        return new EmbeddingModel() {
            @Override
            public float[] embed(String text) {
                return new float[768];
            }

            @Override
            public float[] embed(Document document) {
                return new float[768];
            }

            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                List<Embedding> list = new ArrayList<>();
                int idx = 0;
                for (String text : request.getInstructions()) {
                    list.add(new Embedding(new float[768], idx++));
                }
                return new EmbeddingResponse(list);
            }
        };
    }
}
