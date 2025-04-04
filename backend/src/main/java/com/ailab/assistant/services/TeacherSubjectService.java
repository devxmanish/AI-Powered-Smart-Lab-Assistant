package com.ailab.assistant.services;


import com.ailab.assistant.dtos.FacultyInitialDataDto;
import com.ailab.assistant.dtos.TeacherDto;

import java.util.List;
import java.util.Map;

public interface TeacherSubjectService {
    Map<String, String > assignSubjectToTeacher(long teacherId, long subjectId);

    FacultyInitialDataDto getFacultyInitailData(Long id);

    List<TeacherDto> getSubjectFaclties(long subjectId);
}