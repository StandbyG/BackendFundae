package com.fundae.backend.dto;

import lombok.Data;

@Data
public class ChatBotLogCreateDTO {
    private String pregunta;
    private String respuesta;
    private Integer usuarioId; // Recibimos solo el ID al crear
}