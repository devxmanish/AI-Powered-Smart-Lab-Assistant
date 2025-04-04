//package com.ailab.assistant.models;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Category {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "category", nullable = false)
//    private String category; // it can be text, coding, reasoning etc.
//
//    @ManyToOne
//    @JoinColumn(name = "ai_model_id")
//    private AIModel aiModel;  // Many categories may point to same model
//
//}
