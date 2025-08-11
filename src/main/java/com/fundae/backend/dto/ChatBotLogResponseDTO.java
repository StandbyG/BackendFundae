package com.fundae.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatBotLogResponseDTO {
    private Integer idLog;
    private String pregunta;
    private String respuesta;
    private LocalDateTime fecha;
    private UsuarioDTO usuario; // Enviamos el DTO del usuario para mostrar el nombre
}