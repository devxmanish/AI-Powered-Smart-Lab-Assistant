package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.ExperimentStudentStats;
import com.ailab.assistant.dtos.StudentExperimentStatsDTO;
import com.ailab.assistant.models.ExperimentAccess;
import com.ailab.assistant.models.LabManual;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.ExperimentAccessRepository;
import com.ailab.assistant.repositories.LabManualRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.services.ExperimentAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExperimentAccessServiceImpl implements ExperimentAccessService {

    @Autowired
    private ExperimentAccessRepository repository;

    @Autowired
    private LabManualRepository labManualRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExperimentAccessRepository experimentAccessRepository;

    public void saveAccessRecord(Long studentId, Long experimentId, LocalDateTime startTime) {
        LocalDateTime endTime = LocalDateTime.now();
        long durationMs = Duration.between(startTime, endTime).toMillis();

        LabManual experiment= labManualRepository.findById(experimentId)
                .orElseThrow(()->new RuntimeException("Experiment not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(()->new RuntimeException("Student not found"));

        ExperimentAccess access = new ExperimentAccess();
        access.setStudent(student);
        access.setExperiment(experiment);
        access.setStartTime(startTime);
        access.setEndTime(endTime);
        access.setDurationMs(durationMs);

        repository.save(access);
    }

    @Override
    public List<StudentExperimentStatsDTO> getExperimentStats(Long experimentId) {
        List<ExperimentStudentStats> rawData = experimentAccessRepository.findStudentStatsByExperimentId(experimentId);

        // Group by studentId
        Map<Long, StudentExperimentStatsDTO> studentMap = new HashMap<>();

        for (ExperimentStudentStats record : rawData) {
            studentMap.computeIfAbsent(record.getStudentId(), id -> {
                StudentExperimentStatsDTO studentStats = new StudentExperimentStatsDTO();
                studentStats.setStudentId(id);
                studentStats.setStudentName(record.getStudentName());
                studentStats.setEmail(record.getEmail());
                studentStats.setAccessStats(new ArrayList<>());
                return studentStats;
            }).getAccessStats().add(new StudentExperimentStatsDTO.AccessStats(
                    record.getStartTime(),
                    record.getEndTime(),
                    record.getDurationMs()
            ));
        }

        return new ArrayList<>(studentMap.values());
    }
}
