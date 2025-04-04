package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.AIExplainRequest;
import com.ailab.assistant.dtos.AIGenerateRequest;
import com.ailab.assistant.dtos.LabManualGenerateRequest;
import com.ailab.assistant.services.AIModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.ls.LSOutput;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIModelService implements AIModelService{
    private final WebClient webClient;

//    @Value("${openai.api.url}")
    private String openAiUrl = "https://api.openai.com/v1/chat/completions";

//    @Value("${openai.api.key}")
    private String apiKey= "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";

    public OpenAIModelService() {
        this.webClient = WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<String> generateContent(AIGenerateRequest request) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", "Generate content for category: " + request.category() +
                                ", subject: " + request.subject() + ", topic: " + request.topic() +
                                " with keywords: " + request.additionalKeywords())
                ),
                "max_tokens", 100
        );

        return webClient.post()
                .uri(openAiUrl)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("OpenAI API Error: " + errorBody)));
                })
                .bodyToMono(String.class);
    }

    @Override
    public Mono<String> generateLabManual(LabManualGenerateRequest request) {
        return null;
    }

    @Override
    public Mono<String> explainPartOfContent(AIExplainRequest request) {
        return null;
    }
}

