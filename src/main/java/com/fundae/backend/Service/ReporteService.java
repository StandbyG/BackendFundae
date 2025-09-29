package com.fundae.backend.Service;

import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Repository.VerificacionRepository;
import com.fundae.backend.dto.ReporteCumplimientoDTO;
import com.fundae.backend.dto.ReporteTiemposDTO;
import com.fundae.backend.dto.ReporteVencidosDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {


    private final AjusteRazonableRepository repo;


    public ReporteService(AjusteRazonableRepository repo) {
        this.repo = repo;
    }


    public ReporteCumplimientoDTO cumplimiento(Integer usuarioId, String empresa, LocalDate desde, LocalDate hasta) {
        Object[] row = (Object[]) repo.resumenCumplimiento(usuarioId, empresa, desde, hasta);
        long total = toLong(row[0]);
        long pend = toLong(row[1]);
        long imple = toLong(row[2]);
        long rech = toLong(row[3]);
        double porcentaje = total == 0 ? 0.0 : (imple * 100.0) / total;
        return new ReporteCumplimientoDTO(total, pend, imple, rech, round2(porcentaje));
    }


    public ReporteVencidosDTO vencidos(LocalDate fechaCorte, Integer usuarioId, String empresa) {
        Object[] row = (Object[]) repo.resumenVencidos(fechaCorte, usuarioId, empresa);
        long venc = toLong(row[0]);
        long pend = toLong(row[1]);
        long aprob = toLong(row[2]);
        long rech = toLong(row[3]);
        return new ReporteVencidosDTO(fechaCorte, venc, pend, aprob, rech);
    }


    public ReporteTiemposDTO tiempos(Integer usuarioId, String empresa, LocalDate desde, LocalDate hasta) {
        Object[] row = (Object[]) repo.resumenTiempos(usuarioId, empresa, desde, hasta);
        Double avg = toDouble(row[0]);
        Integer p50 = toInt(row[1]);
        Integer p90 = toInt(row[2]);
        return new ReporteTiemposDTO(avg, p50, p90);
    }


    private long toLong(Object o) {
        if (o == null) return 0L;
        if (o instanceof BigInteger bi) return bi.longValue();
        if (o instanceof Number n) return n.longValue();
        return Long.parseLong(o.toString());
    }


    private Double toDouble(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal bd) return bd.doubleValue();
        if (o instanceof Number n) return n.doubleValue();
        return Double.valueOf(o.toString());
    }


    private Integer toInt(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal bd) return bd.intValue();
        if (o instanceof Number n) return n.intValue();
        return Integer.valueOf(o.toString());
    }


    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}