package com.fundae.backend.dto;

public class PasswordResetTokenDTO {
    public record ForgotPasswordRequest(String email) {}
    public record ResetPasswordRequest(String token, String newPassword) {}
    public record ApiMessage(String message) {}
}
