package com.fundae.backend.dto;

import java.time.LocalDateTime;

public class feedback {
    public record FeedbackCreateRequest(
            Integer ajusteId,
            Short calificacion,
            String comentario,
            Boolean visibleEmpleador
    ){}
    public record FeedbackResponse(
            Integer id,
            Integer ajusteId,
            Integer autorAdminId,
            Short calificacion,
            String comentario,
            Boolean visibleEmpleador,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
