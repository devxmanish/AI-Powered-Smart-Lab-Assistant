package com.ailab.assistant.repositories;

import com.ailab.assistant.dtos.SubjectStudentCountDto;
import com.ailab.assistant.models.StudentSubject;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
    List<StudentSubject> findByStudent(User student);
    List<StudentSubject> findBySubject(Subject subject);

    StudentSubject findByStudentAndSubject(User student, Subject subject);

    List<StudentSubject> findBySubjectIdAndAssignedByTeacher_UserId(long subjectId, Long teacherId);

    List<StudentSubject> findByStudent_UserId(Long studentId);

    @Query("SELECT new com.ailab.assistant.dtos.SubjectStudentCountDto(s.id, s.subjectName, s.subjectCode, COUNT(ss.student.id)) " +
            "FROM StudentSubject ss " +
            "JOIN ss.subject s " +
            "WHERE ss.assignedByTeacher.id = :teacherId " +
            "GROUP BY s.id, s.subjectName, s.subjectCode")
    List<SubjectStudentCountDto> getSubjectWiseStudentCount(@Param("teacherId") Long teacherId);
}