package com.ailab.assistant.services;

import com.ailab.assistant.models.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface QueryService {

    Query saveQuery(Query query);

    List<Query> getQueriesForReceiver(Long receiverId);

    List<Query> getQueriesForSender(Long senderId);
    Optional<Query> getQueryById(Long queryId);

    Query respondToQuery(Long queryId, String response);
}
