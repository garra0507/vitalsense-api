package com.biotech.vitalsenseapi.assistant.controller;

import com.biotech.vitalsenseapi.assistant.dto.ChatRequest;
import com.biotech.vitalsenseapi.assistant.dto.ChatResponse;
import com.biotech.vitalsenseapi.assistant.service.AssistantService;
import com.biotech.vitalsenseapi.assistant.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AssistantServiceController {

    private final AssistantService assistantService;
    private final SupportService supportService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return assistantService.processChat(request);
    }

    @PostMapping("/support/ask")
    public ChatResponse supportAsk(@RequestBody ChatRequest request) {
        String replyText = supportService.ask(request.getMessage());
        return ChatResponse.builder()
                .reply(replyText)
                .build();
    }

    @PostMapping("/support/ingest")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> supportIngest() throws IOException {
        int chunks = supportService.ingest();
        return Map.of("chunksIngested", chunks);
    }
}
