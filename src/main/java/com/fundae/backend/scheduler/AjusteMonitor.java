package com.fundae.backend.scheduler;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AjusteMonitor {

    private final AjusteRazonableRepository ajusteRepo;

    @Scheduled(cron = "0 0 7 * * *")
    public void verificarAjustesVencidos() {
        List<AjusteRazonable> vencidos = ajusteRepo
                .findByEstadoAndFechaImplementacionBeforeAndAlertadoFalse("pendiente", LocalDate.now());

        for (AjusteRazonable ajuste : vencidos) {
            // Aquí puedes notificar vía log o email
            log.warn("Ajuste vencido para usuario ID {}: {}",
                    ajuste.getUsuario().getIdUsuario(), ajuste.getDescripcion());

            // Marcar como alertado
            ajuste.setAlertado(true);
            ajusteRepo.save(ajuste);
        }
    }

}
