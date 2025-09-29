package com.fundae.backend.Controller;

import com.fundae.backend.Model.Alerta;
import com.fundae.backend.Repository.AlertaRepository;
import com.fundae.backend.Service.AlertaService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaRepository repo;
    private final AlertaService alertaService;

    public AlertaController(AlertaRepository repo, AlertaService alertaService) { this.repo = repo;
        this.alertaService = alertaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('administrador')")
    public List<Alerta> listar(
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean leido
    ) {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @PatchMapping("/{id}/leer")
    @PreAuthorize("hasRole('administrador')")
    public ResponseEntity<?> marcarLeido(@PathVariable Integer id) {
        var a = repo.findById(id).orElseThrow();
        a.setLeido(true);
        repo.save(a);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/generar")
    public ResponseEntity<?> generar() {
        alertaService.generarAlertasDiarias();
        return ResponseEntity.ok(Map.of("status","ok","mensaje","Alertas generadas manualmente"));
    }
}
