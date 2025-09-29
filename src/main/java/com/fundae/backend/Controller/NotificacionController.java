package com.fundae.backend.Controller;


import com.fundae.backend.Model.Notificacion;
import com.fundae.backend.Repository.NotificacionRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionRepository repo;

    public NotificacionController(NotificacionRepository r) { this.repo = r; }

    @GetMapping
    @PreAuthorize("hasRole('empleador')")
    public Page<Notificacion> mias(
            @RequestParam Integer empleadorId,
            @RequestParam(defaultValue="false") Boolean leido,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size)
            {
                return repo.findByUsuario_IdUsuarioAndLeidoOrderByCreatedAtDesc(
                        empleadorId, leido, PageRequest.of(page, size)
                );    }

    @PatchMapping("/{id}/leer")
    @PreAuthorize("hasRole('empleador')")
    public ResponseEntity<?> leer(
            @PathVariable Integer id,
            @RequestParam Integer empleadorId) {
        var n = repo.findById(id).orElseThrow();
        if (!n.getUsuario().getIdUsuario().equals(empleadorId)) return ResponseEntity.status(403).build();
        n.setLeido(true);
        repo.save(n);
        return ResponseEntity.ok().build();
    }
}
