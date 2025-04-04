package com.ailab.assistant.dtos;

public record AssignedExperimentDto(
        long id,
        int experimentNo,
        String experimentTitle,
        Long assignedDate,
        Long deadlineDate
) {
}
