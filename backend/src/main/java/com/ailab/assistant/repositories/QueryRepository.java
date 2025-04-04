package com.ailab.assistant.repositories;

import com.ailab.assistant.models.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findByReceiverId(Long receiverId); // Fetch queries assigned to faculty/admin
    List<Query> findBySenderId(Long senderId); // Fetch queries raised by a user
}
