package com.ailab.assistant.controllers;

import com.ailab.assistant.dtos.AIExplainRequest;
import com.ailab.assistant.dtos.AIGenerateRequest;
import com.ailab.assistant.dtos.LabManualGenerateRequest;
import com.ailab.assistant.services.impl.GeminiAIModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class AIModelController {

    @Autowired
    private GeminiAIModelService geminiAIModelService;

    private static final Logger logger = LoggerFactory.getLogger(AIModelController.class);

//    @CrossOrigin(origins = "http://localhost:5173")
    @Secured({"ROLE_FACULTY","ROLE_STUDENT"})
    @PostMapping("/generate")
    public ResponseEntity<String> generateContent(@RequestBody AIGenerateRequest request){
        Mono<String> response = geminiAIModelService.generateContent(request);
        return ResponseEntity.ok(response.block());
    }

    @Secured("ROLE_FACULTY")
    @PostMapping("/generate-labmanual")
    public ResponseEntity<String> generateLabManual(@RequestBody LabManualGenerateRequest request){
        Mono<String> response = geminiAIModelService.generateLabManual(request);
        return ResponseEntity.ok(response.block());
    }

    @Secured({"ROLE_FACULTY","ROLE_STUDENT"})
    @PostMapping("/explain")
    public ResponseEntity<String> explainContent(@RequestBody AIExplainRequest request){
        Mono<String> response = geminiAIModelService.explainPartOfContent(request);
        return ResponseEntity.ok(response.block());
    }

}
