package com.ailab.assistant.dtos;

import com.ailab.assistant.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExperimentStats {
    private Subject subject;
    private Long experimentCount;
    private Long finalCount;
    private Long nonFinalCount;

}