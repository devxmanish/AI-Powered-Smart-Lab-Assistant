package com.ailab.assistant.services;


import com.ailab.assistant.dtos.AddSubjectRequestDTO;
import com.ailab.assistant.dtos.SubjectDto;
import com.ailab.assistant.models.Department;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    Map<String, String> addSubject(AddSubjectRequestDTO subjectDTO, Department department);

    List<SubjectDto> getAllSubjects(Department department);
}