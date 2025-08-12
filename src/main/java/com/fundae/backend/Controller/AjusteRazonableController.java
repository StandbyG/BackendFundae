package com.fundae.backend.Controller;


import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Service.AjusteRazonableService;
import com.fundae.backend.Service.UsuarioService;
import com.fundae.backend.dto.AjusteEstadoUpdateDTO;
import com.fundae.backend.dto.AjusteRazonableCreateDTO;
import com.fundae.backend.dto.AjusteRazonableResponseDTO;
import com.fundae.backend.dto.AjusteRazonableUpdateDTO;
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
    public List<AjusteRazonableResponseDTO> getAll() {
        return ajusteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteRazonableResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ajusteService.getById(id));
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AjusteRazonableResponseDTO>> getByUsuarioId(@PathVariable Integer idUsuario) {
        List<AjusteRazonableResponseDTO> ajustes = ajusteService.getByUsuarioId(idUsuario);
        return ResponseEntity.ok(ajustes);
    }

    @PostMapping("/create")
    public ResponseEntity<AjusteRazonable> create(@RequestBody AjusteRazonableCreateDTO ajusteDTO) {
        // 1. Busca el objeto Usuario completo usando el ID del DTO
        Usuario usuario = usuarioService.getUsuarioById(ajusteDTO.getUsuarioId());

        // 2. Crea una nueva entidad AjusteRazonable
        AjusteRazonable nuevoAjuste = new AjusteRazonable();

        // 3. Copia los datos del DTO a la nueva entidad
        nuevoAjuste.setTipoAjuste(ajusteDTO.getTipoAjuste());
        nuevoAjuste.setDescripcion(ajusteDTO.getDescripcion());
        nuevoAjuste.setEstado(ajusteDTO.getEstado());
        nuevoAjuste.setFechaRecomendacion(ajusteDTO.getFechaRecomendacion());
        nuevoAjuste.setFechaImplementacion(ajusteDTO.getFechaImplementacion());

        // 4. Asigna el objeto Usuario completo a la entidad
        nuevoAjuste.setUsuario(usuario);

        // 5. Guarda la nueva entidad en la base de datos
        AjusteRazonable ajusteGuardado = ajusteService.save(nuevoAjuste);

        return ResponseEntity.status(201).body(ajusteGuardado);
    }


    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AjusteRazonable>> getByEstado(@PathVariable String estado) {
        List<AjusteRazonable> ajustes = ajusteService.getByEstado(estado);
        return ResponseEntity.ok(ajustes);
    }

    @PostMapping("/create-bulk")
    public ResponseEntity<List<AjusteRazonable>> createBulk(@RequestBody List<AjusteRazonableCreateDTO> ajustesDTO) {
        List<AjusteRazonable> ajustesGuardados = ajusteService.saveBulk(ajustesDTO);
        return ResponseEntity.status(201).body(ajustesGuardados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ajusteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AjusteRazonableResponseDTO> updateEstadoYFecha(
            @PathVariable Integer id,
            @RequestBody AjusteEstadoUpdateDTO dto
    ) {
        AjusteRazonableResponseDTO ajusteActualizado = ajusteService.updateEstadoYFecha(id, dto);
        return ResponseEntity.ok(ajusteActualizado);
    }


}
