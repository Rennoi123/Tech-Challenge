package com.example.techchallenge.dto.Request;


public record UpdatePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
