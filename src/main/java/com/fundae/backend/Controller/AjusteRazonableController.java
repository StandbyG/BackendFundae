package com.fundae.backend.Controller;


import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Service.AjusteRazonableService;
import com.fundae.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")
@RequiredArgsConstructor
public class AjusteRazonableController {

    private final AjusteRazonableService ajusteService;
    private final UsuarioService usuarioService;  // Asegúrate de que este servicio esté disponible

    @GetMapping
    public List<AjusteRazonable> getAll() {
        return ajusteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteRazonable> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ajusteService.getById(id));
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AjusteRazonable>> getByUsuarioId(@PathVariable Integer idUsuario) {
        List<AjusteRazonable> ajustes = ajusteService.getByUsuarioId(idUsuario);
        return ResponseEntity.ok(ajustes);
    }


    @PostMapping("/create")
    public ResponseEntity<AjusteRazonable> create(@RequestBody AjusteRazonable ajuste) {
        // Obtener el usuario correspondiente al usuarioId
        Usuario usuario = usuarioService.findById(ajuste.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar el usuario al ajuste
        ajuste.setUsuario(usuario);

        // Guardar el ajuste
        return ResponseEntity.status(201).body(ajusteService.save(ajuste));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AjusteRazonable> update(@PathVariable Integer id, @RequestBody AjusteRazonable ajuste) {
        ajuste.setIdAjuste(id);
        return ResponseEntity.ok(ajusteService.save(ajuste));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AjusteRazonable>> getByEstado(@PathVariable String estado) {
        List<AjusteRazonable> ajustes = ajusteService.getByEstado(estado);
        return ResponseEntity.ok(ajustes);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ajusteService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
