package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ajustes_razonables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjusteRazonable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ajuste")
    private Integer idAjuste;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_ajuste", length = 100)
    private String tipoAjuste;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_recomendacion")
    private LocalDate fechaRecomendacion;

    @Column(name = "fecha_implementacion")
    private LocalDate fechaImplementacion;

    @Column(length = 20)
    private String estado;
    @Column(name = "alertado", nullable = false)
    private boolean alertado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false)
    private OrigenAjuste origen = OrigenAjuste.EMPRESA;

    // Campos propios de ONG
    @Column(length = 100)
    private String espacio;

    @Column(columnDefinition = "text")
    private String observacion;
    @Column(columnDefinition = "text")
    private String ajustesSugeridos;

    @Column(length = 255)
    private String refNormativa;

    @Column(length = 255)
    private String refFotografica;    // URL o path

    @Enumerated(EnumType.STRING)
    private Nivel dificultad;

    @Enumerated(EnumType.STRING)
    private Nivel urgencia;

}
