package com.ailab.assistant.controllers;

import com.ailab.assistant.dtos.*;
import com.ailab.assistant.models.*;
import com.ailab.assistant.repositories.RoleRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.security.response.MessageResponse;
import com.ailab.assistant.security.services.UserDetailsImpl;
import com.ailab.assistant.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    SubjectService subjectService;

    @Autowired
    TeacherSubjectService teacherSubjectService;

    @Autowired
    StudentSubjectService studentSubjectService;

    @Autowired
    private LabManualFormatService labManualFormatService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/admin-initials")
    public ResponseEntity<?> getAdminInitials(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User admin = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("User not found"));
        List<TeacherDto> teachers = userService.getAllTeachers(admin.getDepartment());
        List<StudentDto> students = userService.getAllStudents(admin.getDepartment());
        List<SubjectDto> subjects = subjectService.getAllSubjects(admin.getDepartment());
        Map<String, List<?>> response = Map.of(
                "teachers", teachers,
                "subjects",  subjects,
                "students", students
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject-faculties")
    public ResponseEntity<?> getSubjectFaculties(@RequestParam long subjectId){
        List<TeacherDto> teachers = teacherSubjectService.getSubjectFaclties(subjectId);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/getLabFormatList")
    public ResponseEntity<?> getLabFormatList(){
        List<LabManualFormatListDto> response = labManualFormatService.getAllLabManualFormats();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-subject")
    public ResponseEntity<?> addNewSubject(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody AddSubjectRequestDTO subjectDTO){
        User user = userRepository.findByEmail(userDetails.getEmail())
                        .orElseThrow(()-> new RuntimeException("User not found"));
        Map<String, String> response = subjectService.addSubject(subjectDTO, user.getDepartment());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RegisterStudentRequest registerStudentRequest) {
        User admin = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
        if (userRepository.existsByEmail(registerStudentRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Student already registered."));
        }
        User user = new User(registerStudentRequest.username(),
                registerStudentRequest.email(),
                encoder.encode(registerStudentRequest.password()));
        Role role = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(role);
        user.setDepartment(admin.getDepartment());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Student registered successfully!"));
    }

    @PostMapping("/register-faculty")
    public ResponseEntity<?> registerFaculty(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RegisterStudentRequest registerStudentRequest) {
        User admin = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
        if (userRepository.existsByEmail(registerStudentRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Faculty already registered."));
        }
        User user = new User(registerStudentRequest.username(),
                registerStudentRequest.email(),
                encoder.encode(registerStudentRequest.password()));
        Role role = roleRepository.findByRoleName(AppRole.ROLE_FACULTY)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(role);
        user.setDepartment(admin.getDepartment());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Faculty registered successfully!"));
    }

    /*
//    Get All Teachers
    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User admin = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("User not found"));
        List<TeacherDto> response = userService.getAllTeachers(admin.getDepartment());

        return ResponseEntity.ok(response);
    }

//  Get All Students
    @GetMapping("/subjects")
    public ResponseEntity<?> getAllSubjects(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User admin = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<SubjectResponseDto> response = subjectService.getAllSubjects(admin.getDepartment());

        return ResponseEntity.ok(response);
    }
     */

    @PostMapping("/assign-subject-teacher")
    public ResponseEntity<Map<String, String>> assignSubjectTeacher(@RequestParam Long teacherId, @RequestParam Long subjectId) {
        Map<String, String> response = teacherSubjectService.assignSubjectToTeacher(teacherId, subjectId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/student-subjects")
    public ResponseEntity<?> assignSubjectToStudent(@RequestBody AssignStudentSubjectDto assignStudentSubjectDto){
        Map<String, String> response = studentSubjectService.assignSubjectToStudent(assignStudentSubjectDto);

        return ResponseEntity.ok(response);
    }
}