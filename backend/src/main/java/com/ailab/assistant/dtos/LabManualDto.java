package com.ailab.assistant.dtos;

public record LabManualDto(
        int experimentNo,
        String experimentTitle,
        String content,
        boolean isFinal,
        long subjectId
) {
}
