package com.ailab.assistant.controllers;


import com.ailab.assistant.dtos.LabFormatResponse;
import com.ailab.assistant.services.LabManualFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab")
public class LabManualFormatController {

    @Autowired
    LabManualFormatService labManualFormatService;

    @PostMapping("/manualformat")
    public ResponseEntity<LabFormatResponse> getLabManualFormat(@RequestParam long subjectId){
        System.out.println("Subject Id: "+ subjectId);
            LabFormatResponse response = labManualFormatService.findLabFormatBySubjectId(subjectId);
            return ResponseEntity.ok().body(response);
    }
}
