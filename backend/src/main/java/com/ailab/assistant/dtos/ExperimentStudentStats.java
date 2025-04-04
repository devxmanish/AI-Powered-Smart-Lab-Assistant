package com.ailab.assistant.dtos;

import java.time.LocalDateTime;

public interface ExperimentStudentStats {
    Long getStudentId();
    String getStudentName();
    String getEmail();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    Long getDurationMs();
}
