package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true, length=128)
    private String token;

    @ManyToOne(optional=false)
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @Column(name="expira_en", nullable=false)
    private LocalDateTime expiraEn;

    @Column(nullable=false)
    private boolean usado = false;

    @Column(name="creado_en", nullable=false)
    private LocalDateTime creadoEn = LocalDateTime.now();

}
