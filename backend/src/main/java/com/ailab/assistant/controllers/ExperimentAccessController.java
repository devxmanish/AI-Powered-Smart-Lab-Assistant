package com.ailab.assistant.controllers;

import com.ailab.assistant.dtos.ExperimentAccessDTO;
import com.ailab.assistant.services.ExperimentAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ExperimentAccessController {

    private final ExperimentAccessService accessService;

    @Autowired
    public ExperimentAccessController(ExperimentAccessService accessService) {
        this.accessService = accessService;
    }

    private static final Map<String, LocalDateTime> activeSessions = new ConcurrentHashMap<>();

    @MessageMapping("/startSession")
    @SendTo("/topic/sessionStart")
    public void startSession(ExperimentAccessDTO experimentAccessDTO,@Header("simpSessionAttributes") Map<String, Object> attributes) {
        Long userId = (Long) attributes.get("userId");
        String sessionId = "session:"+userId+":"+experimentAccessDTO.experimentId();
        activeSessions.put(sessionId, LocalDateTime.now());
    }

    @MessageMapping("/endSession")
    public void endSession(ExperimentAccessDTO experimentAccessDTO,@Header("simpSessionAttributes") Map<String, Object> attributes) {
        Long userId = (Long) attributes.get("userId");
        String sessionId = "session:"+userId+":"+experimentAccessDTO.experimentId();
        LocalDateTime startTime = activeSessions.remove(sessionId);
        if (startTime != null) {
            accessService.saveAccessRecord(userId, experimentAccessDTO.experimentId(), startTime);
        }
    }
}
