package com.ailab.assistant.dtos;

public record AddSubjectRequestDTO(
        String subjectName,
        String subjectCode,
        int labManualFormatId
) {
}
