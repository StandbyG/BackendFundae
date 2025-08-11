package com.fundae.backend.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String nombreEmpresa;

}