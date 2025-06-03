package com.example.techchallenge.dto;


public record UpdatePasswrodRequest(
        String oldPassword,
        String newPassword
) {
}
