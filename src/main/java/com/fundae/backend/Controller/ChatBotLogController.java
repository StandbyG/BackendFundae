package com.fundae.backend.Controller;

import com.fundae.backend.Model.ChatBotLog;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Service.ChatBotLogService;
import com.fundae.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-logs")
@RequiredArgsConstructor
public class ChatBotLogController {

    private final ChatBotLogService logService;
    private final UsuarioService usuarioService;

    @GetMapping
    public List<ChatBotLog> getAll() {
        return logService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatBotLog> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(logService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ChatBotLog> create(@RequestBody ChatBotLog log) {
        Usuario usuario = usuarioService.findById(log.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar el usuario al ajuste
        log.setUsuario(usuario);
        return ResponseEntity.status(201).body(logService.save(log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
