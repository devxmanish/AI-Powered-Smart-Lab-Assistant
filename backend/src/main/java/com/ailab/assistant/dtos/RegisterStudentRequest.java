package com.ailab.assistant.dtos;

public record RegisterStudentRequest(
        String username,
        String email,
        String password
) {
}
