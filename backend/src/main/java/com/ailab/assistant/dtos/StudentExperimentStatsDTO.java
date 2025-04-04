package com.ailab.assistant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentExperimentStatsDTO {
    private Long studentId;
    private String studentName;
    private String email;
    private List<AccessStats> accessStats;

    @Data
    @AllArgsConstructor
    public static class AccessStats {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long durationMs;
    }
}
