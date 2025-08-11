package com.fundae.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AjusteRazonableUpdateDTO {
    private String tipoAjuste;
    private String descripcion;
    private LocalDate fechaRecomendacion;
    private LocalDate fechaImplementacion;
    private String estado;
    private boolean alertado;
}