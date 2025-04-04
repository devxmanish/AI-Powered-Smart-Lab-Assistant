package com.ailab.assistant.repositories;

import com.ailab.assistant.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {}