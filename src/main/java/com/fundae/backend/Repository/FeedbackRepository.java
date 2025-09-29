package com.fundae.backend.Repository;

import com.fundae.backend.Model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Optional<Feedback> findByAjuste_IdAjuste(Integer ajusteId);

    List<Feedback> findByAjuste_IdAjusteOrderByCreatedAtDesc(Integer ajusteId);
    @Query("""
        SELECT f FROM Feedback f
        JOIN f.ajuste a
        WHERE a.usuario.idUsuario = :empleadorId
        AND (:soloVisibles = false OR f.visibleEmpleador = true)
    """)
    Page<Feedback> pageByEmpleador(@Param("empleadorId") Integer empleadorId,
                                   @Param("soloVisibles") boolean soloVisibles, Pageable pageable);
}
