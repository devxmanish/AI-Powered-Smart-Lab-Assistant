package com.ailab.assistant.repositories;

import com.ailab.assistant.dtos.ExperimentStudentStats;
import com.ailab.assistant.models.ExperimentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentAccessRepository extends JpaRepository<ExperimentAccess, Long> {
    @Query("""
        SELECT ea.student.userId AS studentId, 
               ea.student.userName AS studentName, 
               ea.student.email AS email, 
               ea.startTime AS startTime, 
               ea.endTime AS endTime, 
               ea.durationMs AS durationMs
        FROM ExperimentAccess ea
        WHERE ea.experiment.id = :experimentId
        ORDER BY ea.student.userId, ea.startTime
    """)
    List<ExperimentStudentStats> findStudentStatsByExperimentId(@Param("experimentId") Long experimentId);
}
