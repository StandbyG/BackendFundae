package com.fundae.backend.Repository;

import com.fundae.backend.Model.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface NotificacionRepository extends JpaRepository<Notificacion,Integer> {
    Page<Notificacion> findByUsuario_IdUsuarioAndLeidoOrderByCreatedAtDesc(
            Integer userId,
            Boolean leido,
            Pageable pageable
    );
}
