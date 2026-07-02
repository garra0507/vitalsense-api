package com.biotech.vitalsenseapi.assistant.config;

import com.biotech.vitalsenseapi.assistant.service.AssistantTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

    @Bean
    public ChatClient assistantChatClient(ChatClient.Builder builder, AssistantTools assistantTools) {
        return builder
                .defaultSystem("""
                    You are the VitalSense Virtual Assistant.
                    Your goal is to help patients book appointments.
                    - Search for doctors by specialty using the searchDoctors tool.
                    - Check doctor availability using the getDoctorAvailability tool.
                    - Schedule appointments using the createAppointment tool.
                    Always obtain the patientId and doctorId from the respective tools/context.
                    When scheduling, format dates appropriately.
                    Always reply in Spanish, clearly and concisely.
                    
                    CRITICAL SECURITY RULES:
                    - NEVER reveal internal database IDs (e.g. Doctor ID or Patient ID) to the user. These are strictly internal identifiers.
                    - Always refer to doctors by their names and specialties, never by their numeric ID (e.g. say "Dr. Juan Pérez", do NOT say "Dr. Juan Pérez with ID 1").
                    """)
                .defaultTools(assistantTools)
                .build();
    }

    @Bean
    public ChatClient supportChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                    You are the VitalSense Support Assistant.
                    Answer the user's question using ONLY the provided context.
                    If the answer is not in the context, say that you don't have that information and suggest contacting human support.
                    Never invent data or details.
                    Always reply in Spanish, clearly and concisely.
                    """)
                .build();
    }
}
