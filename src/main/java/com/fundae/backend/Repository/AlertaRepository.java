package com.fundae.backend.Repository;

import com.fundae.backend.Model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
    Optional<Alerta> findByUniqueKey(String uniqueKey);
}