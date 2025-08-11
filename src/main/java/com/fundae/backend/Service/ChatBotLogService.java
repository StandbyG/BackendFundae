package com.fundae.backend.Service;


import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.ChatBotLog;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.ChatBotLogRepository;
import com.fundae.backend.dto.ChatBotLogResponseDTO;
import com.fundae.backend.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotLogService {

    private final ChatBotLogRepository logRepository;
    private ChatBotLogResponseDTO convertToDto(ChatBotLog log) {
        ChatBotLogResponseDTO dto = new ChatBotLogResponseDTO();
        dto.setIdLog(log.getIdLog());
        dto.setPregunta(log.getPregunta());
        dto.setRespuesta(log.getRespuesta());
        dto.setFecha(log.getFecha());

        if (log.getUsuario() != null) {
            UsuarioDTO usuarioDto = new UsuarioDTO();
            usuarioDto.setIdUsuario(log.getUsuario().getIdUsuario());
            usuarioDto.setNombre(log.getUsuario().getNombre());
            dto.setUsuario(usuarioDto);
        }
        return dto;
    }
    public List<ChatBotLogResponseDTO> getAll() {
        return logRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<ChatBotLog> getByUsuario(Usuario usuario) {
        return logRepository.findByUsuario(usuario);
    }

    public List<ChatBotLogResponseDTO> getByUsuarioId(Integer usuarioId) {
        return logRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ChatBotLog save(ChatBotLog log) {
        return logRepository.save(log);
    }

    public void delete(Integer id) {
        if (!logRepository.existsById(id)) {
            throw new ResourceNotFoundException("Log no encontrado con ID: " + id);
        }
        logRepository.deleteById(id);
    }
}
