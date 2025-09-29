package com.fundae.backend.Service;

import com.fundae.backend.Model.Alerta;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Repository.AlertaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AlertaService {

    private final AjusteRazonableRepository ajustesRepo;
    private final AlertaRepository alertaRepo;


    public AlertaService(AjusteRazonableRepository ajustesRepo,
                         AlertaRepository alertaRepo) {
        this.ajustesRepo = ajustesRepo;
        this.alertaRepo = alertaRepo;
    }

    @Transactional
    public void generarAlertasDiarias() {
        LocalDate hoy = LocalDate.now();

        for (Object[] row : ajustesRepo.empresasConVencidos()) {
            String empresa = (String) row[0];
            Integer total = ((Number) row[1]).intValue();
            String ukey = "PLAZO_VENCIDO|" + hoy + "|" + empresa;
            if (alertaRepo.findByUniqueKey(ukey).isEmpty()) {
                var alerta = new Alerta();
                alerta.setTipo("PLAZO_VENCIDO");
                alerta.setNombreEmpresa(empresa);
                alerta.setTotalAfectados(total);
                alerta.setMensaje("Hay " + total + " ajustes PENDIENTES vencidos en " + empresa + " al " + hoy);
                alerta.setSeveridad("ALTA");
                alerta.setUniqueKey(ukey);
                alertaRepo.save(alerta);
            }
        }
        for (String empresa : ajustesRepo.empresasSinActividad30d()) {
            String ukey = "INACTIVIDAD30|" + hoy + "|" + empresa;
            if (alertaRepo.findByUniqueKey(ukey).isEmpty()) {
                var alerta = new Alerta();
                alerta.setTipo("INACTIVIDAD");
                alerta.setNombreEmpresa(empresa);
                alerta.setTotalAfectados(0);
                alerta.setPeriodoDesde(hoy.minusDays(30));
                alerta.setPeriodoHasta(hoy);
                alerta.setMensaje("Sin ajustes registrados en los últimos 30 días en " + empresa);
                alerta.setSeveridad("MEDIA");
                alerta.setUniqueKey(ukey);
                alertaRepo.save(alerta);
            }
        }
    }
}