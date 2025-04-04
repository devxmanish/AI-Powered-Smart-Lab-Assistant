package com.ailab.assistant.dtos;

import java.util.List;
import java.util.Map;

public record FacultyInitialDataDto(
        List<String> categories,
        Map<String, List<SubjectDto>> subjectsMapping
) {
}
