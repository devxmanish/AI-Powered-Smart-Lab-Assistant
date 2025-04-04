package com.ailab.assistant.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "lab_manual_formats")
public class LabManualFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category")
    private String formatCategory;

    @Lob
    private String manualFormat;

    @OneToMany(mappedBy = "labManualFormat", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Subject> subject;
}
