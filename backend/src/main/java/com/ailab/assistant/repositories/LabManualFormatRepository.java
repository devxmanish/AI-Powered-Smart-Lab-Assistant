package com.ailab.assistant.repositories;

import com.ailab.assistant.models.LabManualFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabManualFormatRepository extends JpaRepository<LabManualFormat, Integer> {

    String findByFormatCategory(String formatCategory);
}
