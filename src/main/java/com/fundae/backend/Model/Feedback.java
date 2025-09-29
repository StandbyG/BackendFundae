package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ajuste_id", referencedColumnName = "id_ajuste")
    private AjusteRazonable ajuste;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_admin_id")
    private Usuario autorAdmin;

    private Short calificacion;

    @Column(nullable = false) private String comentario;

    @Column(name = "visible_empleador", nullable = false)
    private Boolean visibleEmpleador = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate void onUpd() { this.updatedAt = LocalDateTime.now(); }
}