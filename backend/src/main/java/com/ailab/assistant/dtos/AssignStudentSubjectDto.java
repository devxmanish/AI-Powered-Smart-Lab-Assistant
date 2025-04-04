package com.ailab.assistant.dtos;

public record AssignStudentSubjectDto(
        long studentId,
        long subjectId,
        long teacherId
) {
}
