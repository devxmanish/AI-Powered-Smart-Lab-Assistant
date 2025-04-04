package com.ailab.assistant.dtos;

public record ExperimentUpdateDto(
        long experimentId,
        int experimentNo,
        String title,
        String content,
        boolean isFinalStatus

) {
}
