package com.example.techchallenge.core.dto;


public record UpdatePasswordDTO(
        String oldPassword,
        String newPassword
) {
}
