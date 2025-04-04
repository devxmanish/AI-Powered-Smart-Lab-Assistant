package com.ailab.assistant.services;

import com.ailab.assistant.dtos.StudentDto;
import com.ailab.assistant.dtos.StudentInitials;
import com.ailab.assistant.dtos.TeacherDto;
import com.ailab.assistant.models.Department;
import com.ailab.assistant.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    User registerUser(User user);

    List<TeacherDto> getAllTeachers(Department department);

    List<StudentDto> getAllStudents(Department department);

    StudentInitials getInitialsOfStudent(Long id);

    /*
    UserDTO getUserById(Long id);

    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    void updateAccountLockStatus(Long userId, boolean lock);

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(Long userId, boolean expire);

    void updateAccountEnabledStatus(Long userId, boolean enabled);

    void updateCredentialsExpiryStatus(Long userId, boolean expire);

    void updatePassword(Long userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    */
}