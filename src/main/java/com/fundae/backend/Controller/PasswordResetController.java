package com.fundae.backend.Controller;

import com.fundae.backend.Service.PasswordResetService;
import com.fundae.backend.dto.PasswordResetTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {
    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService s) { this.service = s; }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetTokenDTO.ApiMessage> forgot(@RequestBody @Validated PasswordResetTokenDTO.ForgotPasswordRequest req) throws NoSuchAlgorithmException {
        service.requestReset(req.email());
        return ResponseEntity.ok(new PasswordResetTokenDTO.ApiMessage("Si el correo existe, se envió un enlace de reset."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetTokenDTO.ApiMessage> reset(@RequestBody @Validated PasswordResetTokenDTO.ResetPasswordRequest req) {
        service.resetPassword(req.token(), req.newPassword());
        return ResponseEntity.ok(new PasswordResetTokenDTO.ApiMessage("Contraseña actualizada."));
    }
}