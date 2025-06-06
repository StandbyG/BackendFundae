package com.fundae.backend.Service;


import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.ChatBotLog;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.ChatBotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotLogService {

    private final ChatBotLogRepository logRepository;

    public List<ChatBotLog> getAll() {
        return logRepository.findAll();
    }

    public List<ChatBotLog> getByUsuario(Usuario usuario) {
        return logRepository.findByUsuario(usuario);
    }

    public ChatBotLog getById(Integer id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log no encontrado con ID: " + id));
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
