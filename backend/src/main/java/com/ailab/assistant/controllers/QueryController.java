package com.ailab.assistant.controllers;

import com.ailab.assistant.models.Query;
import com.ailab.assistant.services.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Submit a new query (Student -> Faculty/Admin)
    @PostMapping
    public ResponseEntity<Query> createQuery(@RequestBody Query query) {
        Query savedQuery = queryService.saveQuery(query);

        // Notify Faculty/Admin in real-time
        messagingTemplate.convertAndSend("/topic/queries/" + query.getReceiverId(), savedQuery);

        return ResponseEntity.ok(savedQuery);
    }

    // Get all queries assigned to a Faculty/Admin
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Query>> getQueriesForReceiver(@PathVariable Long receiverId) {
        return ResponseEntity.ok(queryService.getQueriesForReceiver(receiverId));
    }

    // Get all queries raised by a Student/Faculty
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Query>> getQueriesForSender(@PathVariable Long senderId) {
        return ResponseEntity.ok(queryService.getQueriesForSender(senderId));
    }

    // Faculty/Admin responds to a query (Updates the student in real-time)
    @PutMapping("/{queryId}/respond")
    public ResponseEntity<Query> respondToQuery(@PathVariable Long queryId, @RequestBody String response) {
        Query updatedQuery = queryService.respondToQuery(queryId, response);

        // Notify Student in real-time
        messagingTemplate.convertAndSend("/topic/query-response/" + updatedQuery.getSenderId(), updatedQuery);

        return ResponseEntity.ok(updatedQuery);
    }
}