package com.fundae.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data // Lombok para getters y setters
public class AjusteRazonableCreateDTO {
    // Solo los campos necesarios para crear un ajuste
    private String tipoAjuste;
    private String descripcion;
    private LocalDate fechaRecomendacion;
    private LocalDate fechaImplementacion;
    private String estado;
    private Integer usuarioId; // El frontend enviará el ID del usuario aquí
}