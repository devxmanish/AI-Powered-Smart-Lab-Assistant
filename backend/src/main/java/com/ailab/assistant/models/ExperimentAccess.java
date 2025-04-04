package com.ailab.assistant.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "experiment_access")
@Data
public class ExperimentAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "experiment_id", nullable = false)
    private LabManual experiment;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMs; // Store session duration in milliseconds

}
