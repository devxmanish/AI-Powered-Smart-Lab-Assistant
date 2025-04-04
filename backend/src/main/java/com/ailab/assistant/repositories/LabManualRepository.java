package com.ailab.assistant.repositories;

import com.ailab.assistant.dtos.ExperimentStats;
import com.ailab.assistant.dtos.ExperimentStatsByCategory;
import com.ailab.assistant.models.LabManual;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabManualRepository extends JpaRepository<LabManual, Long> {

    List<LabManual> findBySubjectAndTeacher(Subject subject, User teacher);

    List<LabManual> findBySubjectAndTeacherAndIsFinal(Subject subject, User teacher, boolean isFinal);

    List<LabManual> findBySubject(Subject subject);
    List<LabManual> findByTeacher(User teacher);

    @Query("SELECT new com.ailab.assistant.dtos.ExperimentStats(s, " +
            "COUNT(e), " +
            "SUM(CASE WHEN e.isFinal = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN e.isFinal = false THEN 1 ELSE 0 END)) " +
            "FROM LabManual e " +
            "JOIN e.subject s " +
            "WHERE e.teacher.id = :teacherId " +
            "GROUP BY s")
    List<ExperimentStats> getExperimentStatsByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT new com.ailab.assistant.dtos.ExperimentStatsByCategory('Total', s.subjectName, s.subjectCode, COUNT(e)) " +
            "FROM LabManual e JOIN e.subject s WHERE e.teacher.id = :teacherId GROUP BY s.subjectName, s.subjectCode " +
            "UNION ALL " +
            "SELECT new com.ailab.assistant.dtos.ExperimentStatsByCategory('Final', s.subjectName, s.subjectCode, COUNT(e)) " +
            "FROM LabManual e JOIN e.subject s WHERE e.teacher.id = :teacherId AND e.isFinal = true GROUP BY s.subjectName, s.subjectCode " +
            "UNION ALL " +
            "SELECT new com.ailab.assistant.dtos.ExperimentStatsByCategory('Pending', s.subjectName, s.subjectCode, COUNT(e)) " +
            "FROM LabManual e JOIN e.subject s WHERE e.teacher.id = :teacherId AND e.isFinal = false GROUP BY s.subjectName, s.subjectCode")
    List<ExperimentStatsByCategory> getExperimentStatsByCategory(@Param("teacherId") Long teacherId);

    List<LabManual> findBySubject_IdAndTeacher_UserIdAndIsFinalAndAssignedDateIsNotNull(int intExact, Long teacherId, boolean b);
}