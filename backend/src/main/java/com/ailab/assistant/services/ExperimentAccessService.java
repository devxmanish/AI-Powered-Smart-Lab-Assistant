package com.ailab.assistant.services;

import com.ailab.assistant.dtos.StudentExperimentStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ExperimentAccessService {
    void saveAccessRecord(Long studentId, Long experimentId, LocalDateTime startTime);
    List<StudentExperimentStatsDTO> getExperimentStats(Long experimentId);
}
