package com.ailab.assistant.dtos;

public record SavedExperimentsDto(
        long id,
        int experimentNo,
        String content,
        String experimentTitle,
        boolean isFinal,
        Long assignedDate,
        Long deadlineDate
) {
}
