package com.fundae.backend.dto;

public record ReporteTiemposDTO(
        Double promedioDiasImplementacion,
        Integer p50Dias,
        Integer p90Dias
) {}