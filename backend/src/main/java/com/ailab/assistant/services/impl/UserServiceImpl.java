package com.ailab.assistant.services.impl;


import com.ailab.assistant.dtos.*;
import com.ailab.assistant.models.*;
import com.ailab.assistant.repositories.RoleRepository;
import com.ailab.assistant.repositories.StudentSubjectRepository;
import com.ailab.assistant.repositories.SubjectRepository;
import com.ailab.assistant.repositories.UserRepository;
import com.ailab.assistant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StudentSubjectRepository studentSubjectRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(User user){
//        if (user.getPassword() != null)
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
        return null;
    }

    @Override
    public List<TeacherDto> getAllTeachers(Department department) {
        Role role = roleRepository.findByRoleName(AppRole.ROLE_FACULTY)
                .orElseThrow(()-> new RuntimeException("Role not found"));
        List<User> teachers = userRepository.findByRoleAndDepartment(role,department);
        List<TeacherDto> teacherDtos = new ArrayList<>();
        for (User teacher : teachers) {
            teacherDtos.add(new TeacherDto(teacher.getUserId(), teacher.getUserName(), teacher.getEmail()));
        }
        return teacherDtos;
    }

    @Override
    public List<StudentDto> getAllStudents(Department department) {
        Role role = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                .orElseThrow(()-> new RuntimeException("Role not found"));
        List<User> students = userRepository.findByRoleAndDepartment(role,department);
        List<StudentDto> studentDtos = new ArrayList<>();
        for (User student : students) {
            studentDtos.add(new StudentDto(student.getUserId(),student.getUserName(), student.getEmail()));
        }
        return studentDtos;
    }

    @Override
    public StudentInitials getInitialsOfStudent(Long studentId) {
        List<StudentSubject> studentSubjectList = studentSubjectRepository.findByStudent_UserId(studentId);
        List<StudentSubjectDto> studentSubjectDtos = new ArrayList<>();
        for (StudentSubject studentSubject : studentSubjectList) {
            studentSubjectDtos.add(new StudentSubjectDto(new SubjectDto(studentSubject.getSubject().getId(),studentSubject.getSubject().getSubjectName(),studentSubject.getSubject().getSubjectCode()),
                    new TeacherDto(studentSubject.getAssignedByTeacher().getUserId(),studentSubject.getAssignedByTeacher().getUserName(),studentSubject.getAssignedByTeacher().getEmail())));
        }

        // All subject associated to student's department
        User student = userRepository.findById(studentId)
                .orElseThrow(()->new RuntimeException("User not found"));
        List<Subject> subjects = subjectRepository.findByDepartment(student.getDepartment());
        List<SubjectDto> subjectsForLearning = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsForLearning.add(new SubjectDto(subject.getId(), subject.getSubjectName(), subject.getSubjectCode()));
        }

        List<String> categories = List.of("Coding","Textual");
        Map<String, List<SubjectDto>> subjectMapping = Map.of("Coding",subjectsForLearning,
                "Textual",subjectsForLearning);
        return new StudentInitials(studentSubjectDtos, categories, subjectMapping);
    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

    /*@Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserDTO getUserById(Long id) {
//        return userRepository.findById(id).orElseThrow();
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }


     @Override
    public void updateAccountLockStatus(Long userId, boolean lock) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }



    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
    }


    @Override
    public void updatePassword(Long userId, String password) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }

    @Override
    public void generatePasswordResetToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        // Send email to user
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }


    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (resetToken.isUsed())
            throw new RuntimeException("Password reset token has already been used");

        if (resetToken.getExpiryDate().isBefore(Instant.now()))
            throw new RuntimeException("Password reset token has expired");

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }



    @Override
    public GoogleAuthenticatorKey generate2FASecret(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setTwoFactorSecret(key.getKey());
        userRepository.save(user);
        return key;
    }

    @Override
    public boolean validate2FACode(Long userId, int code){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return totpService.verifyCode(user.getTwoFactorSecret(), code);
    }

    @Override
    public void enable2FA(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disable2FA(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
    }

     */


}
