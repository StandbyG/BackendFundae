package com.fundae.backend.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String nombre;
    private String tipoUsuario;
    private String nombreEmpresa;
    private String ruc;
    private String sector;
    private String direccion;
    private String estadoCumplimiento;
}
