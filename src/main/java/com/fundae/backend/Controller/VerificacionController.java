package com.fundae.backend.Controller;

import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Model.Verificacion;
import com.fundae.backend.Service.UsuarioService;
import com.fundae.backend.Service.VerificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verificaciones")
@RequiredArgsConstructor
public class VerificacionController {

    private final VerificacionService verificacionService;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<Verificacion> getAll() {
        return verificacionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Verificacion> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(verificacionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Verificacion> create(@RequestBody Verificacion verificacion) {
        // Obtener el usuario correspondiente al usuarioId
        Usuario usuario = usuarioService.findById(verificacion.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar el usuario al ajuste
        verificacion.setUsuario(usuario);
        return ResponseEntity.status(201).body(verificacionService.save(verificacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Verificacion> update(@PathVariable Integer id, @RequestBody Verificacion verificacion) {
        verificacion.setIdVerificacion(id);
        return ResponseEntity.ok(verificacionService.save(verificacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        verificacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

