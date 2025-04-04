package com.ailab.assistant.repositories;

import com.ailab.assistant.models.Department;
import com.ailab.assistant.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findBySubjectName(String subjectName);

    List<Subject> findByDepartment(Department department);

    Subject findBySubjectCodeAndDepartment(String subjectCode, Department department);
}
