package com.ailab.assistant.controllers;

import com.ailab.assistant.dtos.*;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.StudentSubjectRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.security.services.UserDetailsImpl;
import com.ailab.assistant.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    TeacherSubjectService teacherSubjectService;

    @Autowired
    StudentSubjectService studentSubjectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LabManualService labManualService;

    @Autowired
    private ExperimentAccessService experimentAccessService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/faculty-initials")
    public ResponseEntity<FacultyInitialDataDto> getAssignedSubjects(@AuthenticationPrincipal UserDetailsImpl userDetails){
        FacultyInitialDataDto response =  teacherSubjectService.getFacultyInitailData(userDetails.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Map<String, List<Map<String, Object>>> experimentStats = labManualService.getFormattedExperimentStats(userDetails.getId());
        List<SubjectStudentCountDto> subjectStudentCountDTOS = studentSubjectService.getSubjectWiseStudentCount(userDetails.getId());
        return ResponseEntity.ok(Map.of("experiment",experimentStats, "student", subjectStudentCountDTOS));
    }

    @PostMapping("/save-lab-manual")
    public ResponseEntity<?> uploadLabManual(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody LabManualDto labManualDto) {
        User teacher = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("Teacher not found"));
        labManualService.saveExperiment(labManualDto, teacher);
        return ResponseEntity.ok("Lab Manual saved successfully");
    }

    @GetMapping("/saved-experiments")
    public ResponseEntity<List<SavedExperimentsDto>> getAllSavedLabManual(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam long subjectId){
        User teacher = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("Teacher not found"));
        List<SavedExperimentsDto> response = labManualService.getAllSavedLabManual(subjectId, teacher);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateExperimentStatus(@RequestParam long experimentId,@RequestParam boolean currentStatus){
        labManualService.updateExperimentStatus(experimentId, currentStatus);

        return  ResponseEntity.ok("Experiment status updated successfully");
    }

    @PutMapping("/update-experiment")
    public ResponseEntity<?> updateExperimentDetails(@RequestBody ExperimentUpdateDto experimentUpdateDto){
        labManualService.updateExperiment(experimentUpdateDto);
        return ResponseEntity.ok("Experiment updated successfully");
    }

    @DeleteMapping("/experiment")
    public ResponseEntity<?> deleteExperiment(@RequestParam long experimentId){
        labManualService.deleteExperiment(experimentId);

        return ResponseEntity.ok("Experiment deleted successfully");
    }

//    @GetMapping("/lab-manual-content")
//    public ResponseEntity<?> getLabManualContent(@RequestParam long labManualId){
//        String response =  labManualService.getLabManualContent(labManualId);
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/generate-lab-manual-pdf")
    public ResponseEntity<byte[]> generateLabManual(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam long subjectId) throws Exception {
        User teacher = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("Teacher not found"));

        byte[] pdfBytes = pdfGenerationService.generateLabManual(subjectId,teacher);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_manual.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/assigned-students")
    public ResponseEntity<List<StudentDto>> getAllAssignedStudents(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestParam long subjectId) {
        List<StudentDto> students = studentSubjectService.getAllAssignedStudents(userDetails.getId(), subjectId);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/assign")
    public ResponseEntity<?> assignExperimentToStudents(@RequestBody AssignExperimentDto assignExperimentDto){
        Map<String, String> response = labManualService.assignExperimentToStudents(assignExperimentDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-deadline")
    public ResponseEntity<?> updateExperimentDeadline(@RequestBody AssignExperimentDto assignExperimentDto){
        Map<String, String> response = labManualService.updateExperimentDeadline(assignExperimentDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/experiment-stats")
    public ResponseEntity<?> getExperimentStats(@RequestParam Long experimentId){
        List<StudentExperimentStatsDTO> response = experimentAccessService.getExperimentStats(experimentId);
        return ResponseEntity.ok(response);
    }
}
