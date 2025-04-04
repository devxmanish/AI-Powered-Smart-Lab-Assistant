package com.ailab.assistant.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabManual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int experimentNo;
    private String title;

    @Lob
    private String content;

    private boolean isFinal;

//    unix timestamp
    private Long assignedDate;

//    unix timestamp
    private Long deadlineDate;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "experiment")
    private List<ExperimentAccess> accessRecords;
}