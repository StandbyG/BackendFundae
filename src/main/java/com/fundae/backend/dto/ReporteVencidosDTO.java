package com.fundae.backend.dto;

import java.time.LocalDate;


public record ReporteVencidosDTO(
        LocalDate fechaCorte,
        Long totalVencidos,
        Long totalPendientes,
        Long totalImplementados,
        Long totalRechazados
) {}