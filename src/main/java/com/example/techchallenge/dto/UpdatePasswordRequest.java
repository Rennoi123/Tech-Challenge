package com.example.techchallenge.dto;


public record UpdatePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
