package com.fundae.backend.Controller;


import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Service.AjusteRazonableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")
@RequiredArgsConstructor
public class AjusteRazonableController {

    private final AjusteRazonableService ajusteService;

    @GetMapping
    public List<AjusteRazonable> getAll() {
        return ajusteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteRazonable> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ajusteService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AjusteRazonable> create(@RequestBody AjusteRazonable ajuste) {
        return ResponseEntity.status(201).body(ajusteService.save(ajuste));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AjusteRazonable> update(@PathVariable Integer id, @RequestBody AjusteRazonable ajuste) {
        ajuste.setIdAjuste(id);
        return ResponseEntity.ok(ajusteService.save(ajuste));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ajusteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
