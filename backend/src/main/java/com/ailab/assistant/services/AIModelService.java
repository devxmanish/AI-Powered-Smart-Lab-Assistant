package com.ailab.assistant.services;

import com.ailab.assistant.dtos.AIExplainRequest;
import com.ailab.assistant.dtos.AIGenerateRequest;
import com.ailab.assistant.dtos.LabManualGenerateRequest;
import reactor.core.publisher.Mono;

public interface AIModelService {
    Mono<String> generateContent(AIGenerateRequest request);
    Mono<String> generateLabManual(LabManualGenerateRequest request);
    Mono<String> explainPartOfContent(AIExplainRequest request);
}
