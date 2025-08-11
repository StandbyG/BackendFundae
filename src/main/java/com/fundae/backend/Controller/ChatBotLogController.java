package com.fundae.backend.Controller;

import com.fundae.backend.Model.ChatBotLog;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Service.ChatBotLogService;
import com.fundae.backend.Service.UsuarioService;
import com.fundae.backend.dto.ChatBotLogCreateDTO;
import com.fundae.backend.dto.ChatBotLogResponseDTO;
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
    public List<ChatBotLogResponseDTO> getAll() {
        return logService.getAll();
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<ChatBotLogResponseDTO> getByUsuarioId(@PathVariable Integer usuarioId) {
        return logService.getByUsuarioId(usuarioId);
    }


    @PostMapping
    public ResponseEntity<ChatBotLog> create(@RequestBody ChatBotLogCreateDTO logDTO) {
        Usuario usuario = usuarioService.getUsuarioById(logDTO.getUsuarioId());

        ChatBotLog newLog = new ChatBotLog();
        newLog.setPregunta(logDTO.getPregunta());
        newLog.setRespuesta(logDTO.getRespuesta());
        newLog.setUsuario(usuario);

        return ResponseEntity.status(201).body(logService.save(newLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
