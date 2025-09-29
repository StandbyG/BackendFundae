package com.fundae.backend.Service;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Feedback;
import com.fundae.backend.Model.Notificacion;
import com.fundae.backend.Repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Autowired
    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void notificarInApp(AjusteRazonable ajuste, Feedback feedback) {
        // Crear la notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(ajuste.getUsuario());  // El empleador recibe la notificación
        notificacion.setTipo("FEEDBACK_NUEVO");
        notificacion.setReferenciaId(feedback.getId());
        notificacion.setTitulo("Nueva retroalimentación sobre tu ajuste #" + ajuste.getIdAjuste());
        notificacion.setMensaje(feedback.getComentario());
        notificacionRepository.save(notificacion);  // Guardar la notificación en la base de datos
    }
}