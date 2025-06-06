package com.fundae.backend.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(name = "contraseña_hash", nullable = false)
    private String contraseñaHash;

    private String nombreEmpresa;

    private String ruc;

    private String sector;

    private String direccion;

    private String estadoCumplimiento;

    @Column(name = "tipo_usuario", nullable = false)
    private String tipoUsuario; // valores: "administrador" o "empleador"

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatBotLog> chatbotLogs;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AjusteRazonable> ajustes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Verificacion> verificaciones;
}
