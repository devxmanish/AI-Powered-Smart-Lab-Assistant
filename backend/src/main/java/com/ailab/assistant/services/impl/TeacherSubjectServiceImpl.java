package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.FacultyInitialDataDto;
import com.ailab.assistant.dtos.SubjectDto;
import com.ailab.assistant.dtos.TeacherDto;
import com.ailab.assistant.models.LabManual;
import com.ailab.assistant.models.Subject;
import com.ailab.assistant.models.TeacherSubject;
import com.ailab.assistant.models.User;
import com.ailab.assistant.repositories.LabManualRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.repositories.TeacherSubjectRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.services.TeacherSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeacherSubjectServiceImpl implements TeacherSubjectService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    LabManualRepository labManualRepository;

    @Override
    public Map<String, String> assignSubjectToTeacher(long teacherId, long subjectId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(()-> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        // check is subject assigned already to the teacher
        List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findBySubjectAndTeacher(subject,teacher);

        if(teacherSubjects.isEmpty()){
            TeacherSubject teacherSubject = new TeacherSubject();
            teacherSubject.setSubject(subject);
            teacherSubject.setTeacher(teacher);
            teacherSubjectRepository.save(teacherSubject);
            return Map.of("message","Subject Assigned successfully");
        } else {
            return Map.of("message", "subject already assigned to teacher");
        }

    }

    @Override
    public FacultyInitialDataDto getFacultyInitailData(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(()-> new RuntimeException("Teacher not found"));

        // All subjects associated to teacher
        List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findByTeacher(teacher);
        List<SubjectDto> assignedSubjectsForLab = new ArrayList<>();
        for (TeacherSubject teacherSubject : teacherSubjects) {
            assignedSubjectsForLab.add(new SubjectDto(teacherSubject.getSubject().getId(), teacherSubject.getSubject().getSubjectName(), teacherSubject.getSubject().getSubjectCode()));
        }

        // All subject associated to teacher's department
        List<Subject> subjects = subjectRepository.findByDepartment(teacher.getDepartment());
        List<SubjectDto> subjectsForLearning = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsForLearning.add(new SubjectDto(subject.getId(), subject.getSubjectName(), subject.getSubjectCode()));
        }

        //Subject mappings
        List<String> categories = List.of("Coding", "Lab manual", "Textual");
        Map<String, List<SubjectDto>> subjectsMapping = Map.of(
                "Coding", subjectsForLearning,
                "Lab manual", assignedSubjectsForLab,
                "Textual", subjectsForLearning
        );

        return new FacultyInitialDataDto(categories, subjectsMapping);
    }

    @Override
    public List<TeacherDto> getSubjectFaclties(long subjectId) {
        List<TeacherSubject> teacherSubjectList = teacherSubjectRepository.findBySubjectId(subjectId);
        List<TeacherDto> teacherDtos = new ArrayList<>();
        for (TeacherSubject teacherSubject : teacherSubjectList) {
            teacherDtos.add(new TeacherDto(teacherSubject.getTeacher().getUserId(), teacherSubject.getTeacher().getUserName(), teacherSubject.getTeacher().getEmail()));
        }
        return teacherDtos;
    }
}
