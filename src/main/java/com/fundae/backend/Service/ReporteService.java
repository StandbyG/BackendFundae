package com.fundae.backend.Service;

import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Repository.VerificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final AjusteRazonableRepository ajusteRepo;
    private final VerificacionRepository verificacionRepo;

    public Map<String, Long> getResumenCumplimiento() {
        return ajusteRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        ajuste -> ajuste.getEstado().toLowerCase(),
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getResumenVerificaciones() {
        return verificacionRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        verificacion -> verificacion.getResultado().toLowerCase(),
                        Collectors.counting()
                ));
    }
}
