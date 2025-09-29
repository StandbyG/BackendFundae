package com.fundae.backend.dto;

public record ReporteCumplimientoDTO(
        Long total,
        Long pendientes,
        Long implementado,
        Long rechazados,
        double porcentajeImplementado
) {}