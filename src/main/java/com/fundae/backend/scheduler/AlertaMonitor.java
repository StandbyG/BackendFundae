package com.fundae.backend.scheduler;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Service.AlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AlertaMonitor {

    private final AlertaService alertaService;

    public AlertaMonitor(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // 08:00 America/Lima (cron con TZ)
    @Scheduled(cron = "0 0 8 * * *", zone = "America/Lima")
    public void ejecutar() {
        alertaService.generarAlertasDiarias();
    }

}
