package com.fundae.backend.Service;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Feedback;
import com.fundae.backend.Model.Notificacion;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Repository.FeedbackRepository;
import com.fundae.backend.Repository.NotificacionRepository;
import com.fundae.backend.Repository.UsuarioRepository;
import com.fundae.backend.dto.feedback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final AjusteRazonableRepository ajusteRepo;
    private final UsuarioRepository usuarioRepo;
    private final NotificacionService notifService;  // Inyectamos NotificacionService
    private final JavaMailSender mailSender;

    public FeedbackService(FeedbackRepository f, AjusteRazonableRepository a,
                           UsuarioRepository u, NotificacionService n,  // Asegúrate de inyectar NotificacionService
                           JavaMailSender m) {
        this.feedbackRepo = f;
        this.ajusteRepo = a;
        this.usuarioRepo = u;
        this.notifService = n;  // Asignamos el servicio de notificación
        this.mailSender = m;
    }

    @Transactional
    public feedback.FeedbackResponse crear(Integer adminId, feedback.FeedbackCreateRequest req) {
        var ajuste = ajusteRepo.findById(req.ajusteId())
                .orElseThrow(() -> new IllegalArgumentException("Ajuste no existe"));

        if (!"implementado".equalsIgnoreCase(ajuste.getEstado())) {
            throw new IllegalStateException("Solo se permite feedback para ajustes implementados");
        }

        var admin = usuarioRepo.findById(adminId).orElseThrow(() -> new IllegalArgumentException("Admin no encontrado"));

        var fb = new Feedback();
        fb.setAjuste(ajuste);
        fb.setAutorAdmin(admin);
        fb.setCalificacion(req.calificacion());
        fb.setComentario(Objects.requireNonNullElse(req.comentario(), "").trim());
        fb.setVisibleEmpleador(req.visibleEmpleador() == null || req.visibleEmpleador());

        var saved = feedbackRepo.save(fb);

        notificarPorEmail(ajuste, saved);
        notifService.notificarInApp(ajuste, saved);

        return toResponse(saved);
    }


    private feedback.FeedbackResponse toResponse(Feedback f) {
        return new feedback.FeedbackResponse(
                f.getId(),
                f.getAjuste().getIdAjuste(),
                f.getAutorAdmin().getIdUsuario(),
                f.getCalificacion(),
                f.getComentario(),
                f.getVisibleEmpleador(),
                f.getCreatedAt(),
                f.getUpdatedAt()
        );
    }

    private void notificarPorEmail(AjusteRazonable ajuste, Feedback fb) {
        var destinatario = ajuste.getUsuario().getCorreo();
        if (destinatario == null || destinatario.isBlank()) return;

        var mm = mailSender.createMimeMessage();
        try {
            var helper = new org.springframework.mail.javamail.MimeMessageHelper(mm, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setFrom("no-reply@tudominio.com", "SGARBIAILPD");
            helper.setSubject("Nueva retroalimentación sobre el ajuste #" + ajuste.getIdAjuste());
            helper.setText("""
          <div style="font-family:Arial,sans-serif;line-height:1.5">
            <p>Hola,</p>
            <p>Se registró una retroalimentación del administrador sobre tu ajuste <b>#%d</b>.</p>
            <p><b>Calificación:</b> %s</p>
            <p><b>Comentario:</b> %s</p>
          </div>
        """.formatted(ajuste.getIdAjuste(),
                    fb.getCalificacion() == null ? "-" : fb.getCalificacion(),
                    fb.getComentario()), true);
            mailSender.send(mm);
        } catch (Exception ignored) {}
    }
    @Transactional(readOnly = true)
    public Page<feedback.FeedbackResponse> pageByEmpleador(Integer empleadorId, boolean soloVisibles, Pageable pageable) {
        // Usamos el repositorio de Feedback para hacer la consulta paginada
        return feedbackRepo.pageByEmpleador(empleadorId, soloVisibles, pageable)
                .map(this::toResponse);  // Mapeamos el resultado a la respuesta adecuada
    }
    @Transactional(readOnly = true)
    public feedback.FeedbackResponse verSiEsMio(Integer feedbackId, Integer empleadorId) {
        // Buscamos el feedback por su ID
        var f = feedbackRepo.findById(feedbackId).orElseThrow();

        // Verificamos que el feedback pertenezca al empleador (es decir, que sea el dueño del ajuste)
        var dueñoId = f.getAjuste().getUsuario().getIdUsuario();
        if (!dueñoId.equals(empleadorId)) throw new RuntimeException("No autorizado");

        // Verificamos si el feedback es visible para el empleador
        if (!Boolean.TRUE.equals(f.getVisibleEmpleador())) throw new RuntimeException("No visible");

        // Mapeamos el feedback al formato de respuesta
        return toResponse(f);
    }
    @Transactional
    public feedback.FeedbackResponse actualizar(Integer id, Short calif, String comentario, Boolean visible) {
        // Buscamos el feedback por su ID
        var fb = feedbackRepo.findById(id).orElseThrow();

        // Actualizamos los campos si se proporcionan nuevos valores
        if (calif != null) fb.setCalificacion(calif);
        if (comentario != null) fb.setComentario(comentario.trim());
        if (visible != null) fb.setVisibleEmpleador(visible);

        // Guardamos el feedback actualizado
        var saved = feedbackRepo.save(fb);

        // Devolvemos el feedback actualizado en el formato adecuado
        return toResponse(saved);
    }

}