package com.fundae.backend.Controller;

import java.time.LocalDate;

import com.fundae.backend.Service.ReporteService;
import com.fundae.backend.dto.ReporteCumplimientoDTO;
import com.fundae.backend.dto.ReporteTiemposDTO;
import com.fundae.backend.dto.ReporteVencidosDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reportes")
public class ReporteController {


    private final ReporteService service;


    public ReporteController(ReporteService service) { this.service = service; }


    // Ej: GET /api/reportes/cumplimiento?desde=2025-01-01&hasta=2025-12-31&empresa=FUNDAE
    @GetMapping("/cumplimiento")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYER')")
    public ResponseEntity<ReporteCumplimientoDTO> cumplimiento(
            @RequestParam(required = false) Integer  usuarioId,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(service.cumplimiento(usuarioId, empresa, desde, hasta));
    }


    // Ej: GET /api/reportes/vencidos?fechaCorte=2025-09-01&empresa=FUNDAE
    @GetMapping("/vencidos")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYER')")
    public ResponseEntity<ReporteVencidosDTO> vencidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCorte,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String empresa) {
        return ResponseEntity.ok(service.vencidos(fechaCorte, usuarioId, empresa));
    }


    // Ej: GET /api/reportes/tiempos?desde=2025-01-01&hasta=2025-09-30&empresa=FUNDAE
    @GetMapping("/tiempos")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYER')")
    public ResponseEntity<ReporteTiemposDTO> tiempos(
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(service.tiempos(usuarioId, empresa, desde, hasta));
    }
}