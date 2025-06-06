package com.fundae.backend.Repository;

import com.fundae.backend.Model.Verificacion;
import com.fundae.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VerificacionRepository extends JpaRepository<Verificacion, Integer> {
    List<Verificacion> findByUsuario(Usuario usuario);
    List<Verificacion> findByUsuario_IdUsuario(Integer idUsuario);

}

