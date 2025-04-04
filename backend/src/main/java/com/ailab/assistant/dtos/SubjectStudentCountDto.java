package com.ailab.assistant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectStudentCountDto {
    private Integer subjectId;
    private String subjectName;
    private String subjectCode;
    private Long studentCount;

}
