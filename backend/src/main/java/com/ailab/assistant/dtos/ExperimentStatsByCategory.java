package com.ailab.assistant.dtos;

public class ExperimentStatsByCategory {
    private String category;
    private String subjectName;
    private String subjectCode;
    private Long count;

    public ExperimentStatsByCategory(String category, String subjectName, String subjectCode, Long count) {
        this.category = category;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public Long getCount() {
        return count;
    }
}
