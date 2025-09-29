package com.fundae.backend.Repository;


import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.OrigenAjuste;
import com.fundae.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AjusteRazonableRepository extends JpaRepository<AjusteRazonable, Integer> {
    List<AjusteRazonable> findByUsuario(Usuario usuario);
    List<AjusteRazonable> findByEstado(String estado);
    List<AjusteRazonable> findByUsuario_IdUsuario(Integer idUsuario);
    List<AjusteRazonable> findByEstadoAndFechaImplementacionBefore(String estado, LocalDate fecha);
    List<AjusteRazonable> findByEstadoAndFechaImplementacionBeforeAndAlertadoFalse(String estado, LocalDate fecha);

    @Query(value = """
    SELECT
      COUNT(*) AS total,
      SUM(CASE WHEN ar.estado = 'pendiente'    THEN 1 ELSE 0 END) AS pendientes,
      SUM(CASE WHEN ar.estado = 'implementado' THEN 1 ELSE 0 END) AS implementados,
      SUM(CASE WHEN ar.estado = 'rechazado'    THEN 1 ELSE 0 END) AS rechazados
    FROM ajustes_razonables ar
    JOIN usuarios u ON u.id_usuario = ar.usuario_id
    WHERE (CAST(:usuarioId AS int) IS NULL OR u.id_usuario = CAST(:usuarioId AS int))
      AND (:empresa IS NULL OR u.nombre_empresa = :empresa)
      AND (CAST(:desde AS date) IS NULL OR ar.fecha_recomendacion >= CAST(:desde AS date))
      AND (CAST(:hasta AS date) IS NULL OR ar.fecha_recomendacion <  CAST(:hasta AS date))
    """, nativeQuery = true)
    Object resumenCumplimiento(
            @Param("usuarioId") Integer usuarioId,
            @Param("empresa")   String empresa,
            @Param("desde")     LocalDate desde,
            @Param("hasta")     LocalDate hasta
    );


    // 2.2 Vencidos a una fecha de corte: pendientes cuya fecha_implementacion planificada < corte
    @Query(value = """
SELECT
SUM(CASE WHEN ar.estado = 'pendiente' AND ar.fecha_implementacion < :corte THEN 1 ELSE 0 END) AS Vencidos,
SUM(CASE WHEN ar.estado = 'pendiente' THEN 1 ELSE 0 END) AS Pendientes,
SUM(CASE WHEN ar.estado = 'implementado' THEN 1 ELSE 0 END) AS Implementado,
SUM(CASE WHEN ar.estado = 'rechazado' THEN 1 ELSE 0 END) AS Rechazados
FROM ajustes_razonables ar
JOIN usuarios u ON u.id_usuario = ar.usuario_id
WHERE (:usuarioId IS NULL OR u.id_usuario = :usuarioId)
AND (:empresa IS NULL OR u.nombre_empresa = :empresa)
""", nativeQuery = true)
    Object resumenVencidos(LocalDate corte, Integer usuarioId, String empresa);
    @Query(value = """
    SELECT
      AVG((ar.fecha_implementacion - ar.fecha_recomendacion))::numeric(10,2) AS Promedio_de_días_implementación,
      percentile_cont(0.5) WITHIN GROUP (ORDER BY (ar.fecha_implementacion - ar.fecha_recomendacion)::float) AS p50,
      percentile_cont(0.9) WITHIN GROUP (ORDER BY (ar.fecha_implementacion - ar.fecha_recomendacion)::float) AS p90
    FROM ajustes_razonables ar
    JOIN usuarios u ON u.id_usuario = ar.usuario_id
    WHERE ar.estado = 'implementado'
      AND (CAST(:usuarioId AS int) IS NULL OR u.id_usuario = CAST(:usuarioId AS int))
      AND (:empresa IS NULL OR u.nombre_empresa = :empresa)
      AND (CAST(:desde AS date) IS NULL OR ar.fecha_implementacion >= CAST(:desde AS date))
      AND (CAST(:hasta AS date) IS NULL OR ar.fecha_implementacion <  CAST(:hasta AS date))
    """, nativeQuery = true)
    Object resumenTiempos(
            @Param("usuarioId") Integer usuarioId,
            @Param("empresa")   String empresa,
            @Param("desde")     LocalDate desde,
            @Param("hasta")     LocalDate hasta
    );

    @Query(value = """
    WITH empresas AS (
      SELECT DISTINCT nombre_empresa FROM usuarios WHERE nombre_empresa IS NOT NULL
    ), actividad AS (
      SELECT u.nombre_empresa, COUNT(*) AS total
      FROM ajustes_razonables ar
      JOIN usuarios u ON u.id_usuario = ar.usuario_id
      WHERE ar.fecha_recomendacion >= CURRENT_DATE - INTERVAL '30 days'
        AND ar.fecha_recomendacion <  CURRENT_DATE
      GROUP BY u.nombre_empresa
    )
    SELECT e.nombre_empresa
    FROM empresas e
    LEFT JOIN actividad a ON a.nombre_empresa = e.nombre_empresa
    WHERE COALESCE(a.total, 0) = 0
  """, nativeQuery = true)
    List<String> empresasSinActividad30d();

    @Query(value = """
    SELECT u.nombre_empresa AS empresa, COUNT(*) AS vencidos
    FROM ajustes_razonables ar
    JOIN usuarios u ON u.id_usuario = ar.usuario_id
    WHERE ar.estado = 'pendiente'
      AND ar.fecha_implementacion < CURRENT_DATE
    GROUP BY u.nombre_empresa
    HAVING COUNT(*) > 0
  """, nativeQuery = true)
    List<Object[]> empresasConVencidos();

    List<AjusteRazonable> findByOrigen(OrigenAjuste origen);
    List<AjusteRazonable> findByUsuario_IdUsuarioAndOrigen(Integer idUsuario, OrigenAjuste origen);

}
