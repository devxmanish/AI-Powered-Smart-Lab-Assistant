package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.AddSubjectRequestDTO;
import com.ailab.assistant.dtos.SubjectDto;
import com.ailab.assistant.models.Department;
import com.ailab.assistant.models.LabManualFormat;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.repositories.LabManualFormatRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    LabManualFormatRepository labManualFormatRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Map<String, String> addSubject(AddSubjectRequestDTO subjectDTO, Department department) {

        // check weather the subject already registered
        Subject subject = subjectRepository.findBySubjectCodeAndDepartment(subjectDTO.subjectCode(),department);

        if (subject == null){
            LabManualFormat labManualFormat = labManualFormatRepository.findById(subjectDTO.labManualFormatId())
                    .orElseThrow(()-> new RuntimeException("Lab manual format not found"));
            Subject newSubject = new Subject();
            newSubject.setSubjectName(subjectDTO.subjectName());
            newSubject.setSubjectCode(subjectDTO.subjectCode());
            newSubject.setDepartment(department);
            newSubject.setLabManualFormat(labManualFormat);
            subjectRepository.save(newSubject);
            return Map.of("message", "Subject registered successfully");
        } else {
            return Map.of("message", "Subject already registered");
        }


    }

    @Override
    public List<SubjectDto> getAllSubjects(Department department) {
        List<Subject> subjects = subjectRepository.findByDepartment(department);
        List<SubjectDto> subjectResponseDtos = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectResponseDtos.add(new SubjectDto(subject.getId(),subject.getSubjectName(), subject.getSubjectCode()));
        }
        return subjectResponseDtos;
    }
}
