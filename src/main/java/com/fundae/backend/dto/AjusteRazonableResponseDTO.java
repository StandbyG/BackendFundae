package com.fundae.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AjusteRazonableResponseDTO {
    private Integer idAjuste;
    private String tipoAjuste;
    private String descripcion;
    private LocalDate fechaRecomendacion;
    private LocalDate fechaImplementacion;
    private String estado;
    private boolean alertado;
    private UsuarioDTO usuario;
    private String origen;
    private String ajustesSugeridos;
    private String espacio;
    private String refNormativa;
    private String refFotografica;
    private String dificultad;
    private String urgencia;
}