package com.ailab.assistant.dtos;

public record StudentSubjectDto(
        SubjectDto subject,
        TeacherDto teacher
) {
}
