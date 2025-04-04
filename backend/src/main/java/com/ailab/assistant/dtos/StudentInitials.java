package com.ailab.assistant.dtos;

import java.util.List;
import java.util.Map;

public record StudentInitials(
        List<?> assignedSubjects,
        List<String> categories,
        Map<String, List<SubjectDto>> subjectsMapping
) {
}
