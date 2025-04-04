package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.*;
import com.ailab.assistant.models.LabManual;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.LabManualRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.services.LabManualService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LabManualServiceImpl implements LabManualService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    LabManualRepository labManualRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveExperiment(LabManualDto labManualDto, User teacher) {
        Subject subject = subjectRepository.findById(labManualDto.subjectId())
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        LabManual labManual = new LabManual();
        labManual.setExperimentNo(labManualDto.experimentNo());
        labManual.setTitle(labManualDto.experimentTitle());
        labManual.setContent(labManualDto.content());
        labManual.setFinal(labManualDto.isFinal());
        labManual.setSubject(subject);
        labManual.setTeacher(teacher);

        labManualRepository.save(labManual);
    }

    @Override
    public List<SavedExperimentsDto> getAllSavedLabManual(long subjectId, User teacher) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new RuntimeException("Subject not found"));
        List<LabManual> labManuals = labManualRepository.findBySubjectAndTeacher(subject, teacher);

        List<SavedExperimentsDto> savedLabManualDtos = new ArrayList<>();
        for (LabManual labManual : labManuals) {
            savedLabManualDtos.add(new SavedExperimentsDto(labManual.getId(), labManual.getExperimentNo(), labManual.getContent(), labManual.getTitle(), labManual.isFinal(), labManual.getAssignedDate(), labManual.getDeadlineDate()));
        }
        return savedLabManualDtos;
    }

    @Override
    public String getLabManualContent(long labManualId) {
        LabManual labManual = labManualRepository.findById(labManualId)
                .orElseThrow(()-> new RuntimeException("Lab manual not found"));
        return labManual.getContent();
    }

    @Override
    public void updateExperimentStatus(long experimentId, boolean currentStatus) {
        LabManual experiment = labManualRepository.findById(experimentId)
                .orElseThrow(()-> new RuntimeException("Experiment not found with id: "+ experimentId));
        experiment.setFinal(!currentStatus);
        labManualRepository.save(experiment);
    }

    @Override
    public void updateExperiment(ExperimentUpdateDto experimentUpdateDto) {
        LabManual experiment = labManualRepository.findById(experimentUpdateDto.experimentId())
                .orElseThrow(()-> new RuntimeException("Experiment not found with id: "+ experimentUpdateDto.experimentId()));
        experiment.setExperimentNo(experimentUpdateDto.experimentNo());
        experiment.setTitle(experimentUpdateDto.title());
        experiment.setContent(experimentUpdateDto.content());
        experiment.setFinal(experimentUpdateDto.isFinalStatus());
        labManualRepository.save(experiment);
    }

    @Override
    public void deleteExperiment(long experimentId) {
        labManualRepository.deleteById(experimentId);
    }

    @Override
    public List<ExperimentStats> getExperimentStatsByTeacher(long teacherId) {
        return labManualRepository.getExperimentStatsByTeacher(teacherId);
    }

    public Map<String, List<Map<String, Object>>> getFormattedExperimentStats(Long teacherId) {
        List<ExperimentStatsByCategory> stats = labManualRepository.getExperimentStatsByCategory(teacherId);

        return stats.stream()
                .collect(Collectors.groupingBy(
                        ExperimentStatsByCategory::getCategory, // Group by category (Total, Final, Non-Final)
                        Collectors.mapping(stat -> Map.of(
                                "subjectName", stat.getSubjectName(),
                                "subjectCode", stat.getSubjectCode(),
                                "count", stat.getCount()
                        ), Collectors.toList()) // Transform each entry into a Map
                ));
    }

    @Override
    public Map<String, String> assignExperimentToStudents(AssignExperimentDto assignExperimentDto) {
        LabManual experiment= labManualRepository.findById(assignExperimentDto.experimentId())
                .orElseThrow(()-> new RuntimeException("Experiment not found"));
        if(!experiment.isFinal()){
            return Map.of("message", "Error: Experiment is not finalized");
        } else if (!(experiment.getAssignedDate() == null)) {
            return Map.of("message", "Error: Experiment already assigned");
        }
        experiment.setAssignedDate(assignExperimentDto.assignedDate());
        experiment.setDeadlineDate(assignExperimentDto.deadlineDate());
        labManualRepository.save(experiment);

        return Map.of("message", "Experiment Assigned successfully");
    }

    @Override
    public List<AssignedExperimentDto> getAllExperimentsAssignedToStudent(Long subjectId, Long teacherId) {
        List<LabManual> labManualList = labManualRepository.findBySubject_IdAndTeacher_UserIdAndIsFinalAndAssignedDateIsNotNull(Math.toIntExact(subjectId),teacherId,true);
        List<AssignedExperimentDto> assignedExperimentDtos = new ArrayList<>();
        for (LabManual labManual : labManualList) {
                assignedExperimentDtos.add(new AssignedExperimentDto(labManual.getId(),labManual.getExperimentNo(),labManual.getTitle(),labManual.getAssignedDate(),labManual.getDeadlineDate()));
        }

        assignedExperimentDtos.sort(Comparator.comparing(AssignedExperimentDto::experimentNo));

        return assignedExperimentDtos;
    }

    @Override
    public Map<String, String> accessExperimentContent(Long experimentId) {
        LabManual experiment = labManualRepository.findById(experimentId)
                .orElseThrow(()-> new RuntimeException("Experiment not found with id: "+ experimentId));
        LocalDateTime localDateTime = LocalDateTime.now();
        Long currentUnixTime = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        if(experiment.getDeadlineDate()>= currentUnixTime){
            return Map.of("content", experiment.getContent());
        }else{
            return Map.of("message","Deadline exceeded");
        }
    }

    @Override
    public Map<String, String> updateExperimentDeadline(AssignExperimentDto assignExperimentDto) {
        LabManual experiment = labManualRepository.findById(assignExperimentDto.experimentId())
                .orElseThrow(()-> new RuntimeException("Experiment not found with id: "+ assignExperimentDto.experimentId()));
        experiment.setDeadlineDate(assignExperimentDto.deadlineDate());
        labManualRepository.save(experiment);
        return Map.of("message", "Deadline updated successfully");
    }
}
