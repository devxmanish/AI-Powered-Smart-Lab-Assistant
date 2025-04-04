package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.AssignStudentSubjectDto;
import com.ailab.assistant.dtos.StudentDto;
import com.ailab.assistant.dtos.SubjectStudentCountDto;
import com.ailab.assistant.models.StudentSubject;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.StudentSubjectRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.services.StudentSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentSubjectServiceImpl implements StudentSubjectService {

    @Autowired
    StudentSubjectRepository studentSubjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Map<String, String> assignSubjectToStudent(AssignStudentSubjectDto assignStudentSubjectDto) {
        User student = userRepository.findById(assignStudentSubjectDto.studentId())
                .orElseThrow(()-> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(assignStudentSubjectDto.subjectId())
                .orElseThrow(()-> new RuntimeException("Subject not found"));
        User teacher = userRepository.findById(assignStudentSubjectDto.teacherId())
                .orElseThrow(()->new RuntimeException("Teacher not found"));

        if(student == null && subject==null && teacher==null){
            return Map.of("message", "Choose correct options. An error occurred");
        }

        StudentSubject assignedSubject = studentSubjectRepository.findByStudentAndSubject(student,subject);
        if(!(assignedSubject == null)){
            return Map.of("message", "Error: subject already assigned");
        }

        StudentSubject studentSubject = new StudentSubject();
        studentSubject.setSubject(subject);
        studentSubject.setStudent(student);
        studentSubject.setAssignedByTeacher(teacher);
        studentSubjectRepository.save(studentSubject);
        return Map.of("message", "Subject assigned successfully");
    }

    @Override
    public List<StudentDto> getAllAssignedStudents(Long teacherId, long subjectId) {
        List<StudentSubject> studentSubjectList = studentSubjectRepository.findBySubjectIdAndAssignedByTeacher_UserId(subjectId,teacherId);
        List<StudentDto> studentDtos = new ArrayList<>();
        for (StudentSubject studentSubject : studentSubjectList) {
            studentDtos.add(new StudentDto(studentSubject.getStudent().getUserId(), studentSubject.getStudent().getUserName(),studentSubject.getStudent().getEmail()));
        }
        return studentDtos;
    }

    @Override
    public List<SubjectStudentCountDto> getSubjectWiseStudentCount(Long teacherId) {
        List<SubjectStudentCountDto> subjectStudentCountDTOS = studentSubjectRepository.getSubjectWiseStudentCount(teacherId);
        return subjectStudentCountDTOS;
    }
}
