package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tipo;
    private String nombreEmpresa;
    private Integer totalAfectados;
    private LocalDate periodoDesde;
    private LocalDate periodoHasta;
    private String mensaje;
    private String severidad;
    private Boolean leido = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable=false, unique=true) private String uniqueKey;
}
