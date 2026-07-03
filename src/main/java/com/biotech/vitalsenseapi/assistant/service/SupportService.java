package com.biotech.vitalsenseapi.assistant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final VectorStore vectorStore;
    private final ChatClient supportChatClient;

    public String ask(String question) {
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(4).build());

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        return supportChatClient.prompt()
                .user("Contexto:\n" + context + "\n\nPregunta: " + question)
                .call()
                .content();
    }

    public int ingest() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Document> docs = new ArrayList<>();

        // 1. Read PDFs
        try {
            Resource[] pdfFiles = resolver.getResources("classpath:docs/*.pdf");
            for (Resource file : pdfFiles) {
                if (file.exists()) {
                    docs.addAll(new PagePdfDocumentReader(file).get());
                }
            }
        } catch (Exception e) {
            // Ignore if no PDFs found
        }

        // 2. Read Markdown files (.md)
        try {
            Resource[] mdFiles = resolver.getResources("classpath:docs/*.md");
            for (Resource file : mdFiles) {
                if (file.exists()) {
                    org.springframework.ai.reader.TextReader reader = new org.springframework.ai.reader.TextReader(file);
                    docs.addAll(reader.read());
                }
            }
        } catch (Exception e) {
            // Ignore if no Markdown files found
        }

        if (docs.isEmpty()) {
            return 0;
        }

        List<Document> chunks = TokenTextSplitter.builder().build().apply(docs);
        vectorStore.add(chunks);
        return chunks.size();
    }
}
