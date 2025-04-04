package com.ailab.assistant.services;


import com.ailab.assistant.dtos.*;
import com.ailab.assistant.models.User;

import java.util.List;
import java.util.Map;

public interface LabManualService {
    void saveExperiment(LabManualDto labManualDto, User teacher);

    List<SavedExperimentsDto> getAllSavedLabManual(long subjectId, User teacher);

    String getLabManualContent(long labManualId);

    void updateExperimentStatus(long experimentId, boolean currentStatus);

    void updateExperiment(ExperimentUpdateDto experimentUpdateDto);

    void deleteExperiment(long experimentId);

    List<ExperimentStats> getExperimentStatsByTeacher(long teacherId);

    Map<String, List<Map<String, Object>>> getFormattedExperimentStats(Long teacherId);

    Map<String, String> assignExperimentToStudents(AssignExperimentDto assignExperimentDto);

    List<AssignedExperimentDto> getAllExperimentsAssignedToStudent(Long subjectId, Long teacherId);

    Map<String, String> accessExperimentContent(Long experimentId);

    Map<String, String> updateExperimentDeadline(AssignExperimentDto assignExperimentDto);
}