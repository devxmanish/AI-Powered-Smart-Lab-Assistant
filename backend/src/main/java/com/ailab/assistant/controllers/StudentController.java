package com.ailab.assistant.controllers;

import com.ailab.assistant.dtos.AssignedExperimentDto;
import com.ailab.assistant.dtos.SavedExperimentsDto;
import com.ailab.assistant.dtos.StudentInitials;
import com.ailab.assistant.security.services.UserDetailsImpl;
import com.ailab.assistant.services.LabManualService;
import com.ailab.assistant.services.PdfGenerationService;
import com.ailab.assistant.services.UserService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    UserService userService;

    @Autowired
    LabManualService labManualService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/initials")
    public ResponseEntity<?> getAllInitialsData(@AuthenticationPrincipal UserDetailsImpl userDetails){
        StudentInitials response = userService.getInitialsOfStudent(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/experiments")
    public ResponseEntity<?> getAllExperimentsAssignedToStudent(@RequestParam Long subjectId, @RequestParam Long teacherId){
        List<AssignedExperimentDto> response = labManualService.getAllExperimentsAssignedToStudent(subjectId,teacherId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/access")
    public ResponseEntity<?> accessExperimentContent(@RequestParam Long experimentId){
        Map<String, String> response = labManualService.accessExperimentContent(experimentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> getExperimentPdf(@RequestParam Long experimentId) throws DocumentException {
        byte[] pdfBytes = pdfGenerationService.generateExperimentPdf(experimentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=experiment.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}