package com.fundae.backend.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String contraseña;

    private String nombre;
    private String tipoUsuario;
    private String nombreEmpresa;
    private String ruc;
    private String sector;
    private String direccion;
    private String estadoCumplimiento;
}
