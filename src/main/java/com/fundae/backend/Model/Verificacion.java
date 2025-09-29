package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "verificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_verificacion")
    private Integer idVerificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "fecha_verificacion")
    private LocalDate fechaVerificacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(length = 20)
    private String resultado;
    @Transient
    private Integer usuarioId;
}
