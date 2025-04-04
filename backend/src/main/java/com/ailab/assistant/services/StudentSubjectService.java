package com.ailab.assistant.services;


import com.ailab.assistant.dtos.AssignStudentSubjectDto;
import com.ailab.assistant.dtos.StudentDto;
import com.ailab.assistant.dtos.SubjectStudentCountDto;

import java.util.List;
import java.util.Map;

public interface StudentSubjectService {
    Map<String, String> assignSubjectToStudent(AssignStudentSubjectDto assignStudentSubjectDto);

    List<StudentDto> getAllAssignedStudents(Long teacherId, long subjectId);

    List<SubjectStudentCountDto> getSubjectWiseStudentCount(Long teacherId);
}