package com.ailab.assistant.services.impl;

import com.ailab.assistant.models.Query;
import com.ailab.assistant.models.QueryStatus;
import com.ailab.assistant.repositories.QueryRepository;
import com.ailab.assistant.services.EmailService;
import com.ailab.assistant.services.QueryService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private EmailService emailService;

    public Query saveQuery(Query query) {
        return queryRepository.save(query);
    }

    public List<Query> getQueriesForReceiver(Long receiverId) {
        return queryRepository.findByReceiverId(receiverId);
    }

    public List<Query> getQueriesForSender(Long senderId) {
        return queryRepository.findBySenderId(senderId);
    }

    public Optional<Query> getQueryById(Long queryId) {
        return queryRepository.findById(queryId);
    }

    public Query respondToQuery(Long queryId, String response) {
        Optional<Query> optionalQuery = queryRepository.findById(queryId);
        if (optionalQuery.isPresent()) {
            Query query = optionalQuery.get();
            query.setResponse(response);
            query.setStatus(QueryStatus.RESOLVED);
            Query updatedQuery = queryRepository.save(query);

            // Send Email Notification
            try {
                emailService.sendEmail("student@example.com",
                        "Your Query Has Been Answered",
                        "Your query has been answered: " + response);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return updatedQuery;
        }
        throw new RuntimeException("Query not found!");
    }
}
