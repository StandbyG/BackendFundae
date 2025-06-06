package com.fundae.backend.Repository;


import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AjusteRazonableRepository extends JpaRepository<AjusteRazonable, Integer> {
    List<AjusteRazonable> findByUsuario(Usuario usuario);
    List<AjusteRazonable> findByEstado(String estado);
    List<AjusteRazonable> findByUsuario_IdUsuario(Integer idUsuario);
    List<AjusteRazonable> findByEstadoAndFechaImplementacionBefore(String estado, LocalDate fecha);
    List<AjusteRazonable> findByEstadoAndFechaImplementacionBeforeAndAlertadoFalse(String estado, LocalDate fecha);


}
