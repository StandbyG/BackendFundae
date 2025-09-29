package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    private String tipo;
    private Integer referenciaId;
    private String titulo;

    @Column(nullable = false) private String mensaje;

    private Boolean leido = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}