package com.ailab.assistant.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "queries")
public class Query {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId; // Student or Faculty (who raised the query)

    @Column(nullable = false)
    private Long receiverId; // Faculty or Admin (who will respond)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; // Query content

    @Column(columnDefinition = "TEXT")
    private String response; // Response from Faculty/Admin

    @Enumerated(EnumType.STRING)
    private QueryStatus status = QueryStatus.PENDING; // Default status

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
