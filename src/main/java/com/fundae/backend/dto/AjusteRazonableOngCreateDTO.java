package com.fundae.backend.dto;

import com.fundae.backend.Model.Nivel;
import lombok.Data;

@Data
public class AjusteRazonableOngCreateDTO {
    private Integer usuarioId;     // empleador destino (requerido)
    private String espacio;
    private String observacion;    // requerido por ONG
    private String ajustesSugeridos;
    private String refNormativa;
    private String refFotografica; // por ahora URL (upload es siguiente iteración)
    private Nivel dificultad;      // BAJA|MEDIA|ALTA
    private Nivel urgencia;        // BAJA|MEDIA|ALTA

    // opcionales heredados si aplica
    private String estado;               // "pendiente" por defecto
    private String fechaRecomendacion;   // ISO yyyy-MM-dd
    private String fechaImplementacion;  // ISO yyyy-MM-dd
}