package com.ailab.assistant.dtos;

public record AssignExperimentDto(
        long experimentId,
        long assignedDate,
        long deadlineDate
) {
}
