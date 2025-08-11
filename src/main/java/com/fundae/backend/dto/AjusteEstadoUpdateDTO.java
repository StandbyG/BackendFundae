package com.fundae.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AjusteEstadoUpdateDTO {
    private String estado;
    private LocalDate fechaImplementacion;
}