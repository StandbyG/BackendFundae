package com.fundae.backend.Repository;


import com.fundae.backend.Model.ChatBotLog;
import com.fundae.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatBotLogRepository extends JpaRepository<ChatBotLog, Integer> {
    List<ChatBotLog> findByUsuario(Usuario usuario);
}
