package com.ailab.assistant.repositories;

import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.TeacherSubject;
import com.ailab.assistant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    List<TeacherSubject> findByTeacher(User teacher);
    List<TeacherSubject> findBySubject(Subject subject);
    List<TeacherSubject> findBySubjectAndTeacher(Subject subject, User teacher);

    List<TeacherSubject> findBySubjectId(long subjectId);
}