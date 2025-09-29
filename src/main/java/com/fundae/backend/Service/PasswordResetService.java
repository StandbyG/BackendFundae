package com.fundae.backend.Service;

import com.fundae.backend.Model.PasswordResetToken;
import com.fundae.backend.Repository.PasswordResetTokenRepository;
import com.fundae.backend.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetService {
    private final UsuarioRepository usuarioRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;          // o tu NotificacionService
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UsuarioRepository u, PasswordResetTokenRepository t,
                                JavaMailSender m, PasswordEncoder p) {
        this.usuarioRepo = u; this.tokenRepo = t; this.mailSender = m; this.passwordEncoder = p;
    }

    @Transactional
    public void requestReset(String email) throws NoSuchAlgorithmException {
        var user = usuarioRepo.findByCorreo(email).orElse(null);
        if (user == null) return;

        tokenRepo.findAll().stream()
                .filter(t -> !t.isUsado() && t.getUsuario().getIdUsuario().equals(user.getIdUsuario())
                             && t.getExpiraEn().isAfter(LocalDateTime.now()))
                .findAny().ifPresent(existing -> { /* opcional: reusar token */ });

        var token = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(SecureRandom.   getInstanceStrong()
                        .generateSeed(48)); // ~384 bits

        String link = "http://localhost:4200/reset-password?token=" + token;

        var prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUsuario(user);
        prt.setExpiraEn(LocalDateTime.now().plusMinutes(30));
        tokenRepo.save(prt);

        // Email
        var msg = new org.springframework.mail.SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("gabrielmprada1@hotmail.com");
        msg.setSubject("Restablecimiento de contraseña");
        msg.setText("""
            Hola,
            Solicitaste restablecer tu contraseña.
            Enlace (vence en 30 minutos):
            %s

            Si no fuiste tú, ignora este correo.
            """.formatted(link));

        mailSender.send(msg);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        var prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (prt.isUsado() || prt.getExpiraEn().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Token expirado o usado");

        var user = prt.getUsuario();
        validatePassword(newPassword); // reglas mínimas

        user.setContraseñaHash(passwordEncoder.encode(newPassword));
        usuarioRepo.save(user);

        prt.setUsado(true);
        tokenRepo.save(prt);

        // Opcional: invalidar refresh tokens / revocar sesiones
    }

    private void validatePassword(String p) {
        if (p == null || p.length() < 8) throw new IllegalArgumentException("Contraseña insegura");
        if (!p.matches(".*[A-Z].*")) throw new IllegalArgumentException("Incluye mayúsculas");
        if (!p.matches(".*[a-z].*")) throw new IllegalArgumentException("Incluye minúsculas");
        if (!p.matches(".*\\d.*"))   throw new IllegalArgumentException("Incluye dígitos");
    }
}
