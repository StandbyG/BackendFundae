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
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
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
    private String estado; // valores: pendiente, implementado, en revisión
    @Column(name = "alertado", nullable = false)
    private boolean alertado = false;
    @Transient
    private Integer usuarioId;
}
