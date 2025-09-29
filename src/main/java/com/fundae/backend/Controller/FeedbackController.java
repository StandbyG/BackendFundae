package com.fundae.backend.Controller;

import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Feedback;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.Repository.FeedbackRepository;
import com.fundae.backend.Service.FeedbackService;
import com.fundae.backend.Service.NotificacionService;
import com.fundae.backend.dto.feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    private final AjusteRazonableRepository ajusteRazonableRepository;

    public FeedbackController(AjusteRazonableRepository ajusteRazonableRepository) {
        this.ajusteRazonableRepository = ajusteRazonableRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('administrador')")
    public ResponseEntity<?> crearFeedback(
            @RequestParam Integer adminId,
            @RequestBody feedback.FeedbackCreateRequest feedbackRequest) {

        try {
            var feedback = feedbackService.crear(adminId, feedbackRequest);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/mios")
    @PreAuthorize("hasRole('empleador')")
    public Page<feedback.FeedbackResponse> misFeedbacks(
            @RequestParam Integer empleadorId,
            @RequestParam(defaultValue = "true") boolean soloVisibles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return feedbackService.pageByEmpleador(empleadorId, soloVisibles, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('empleador')")
    public feedback.FeedbackResponse ver(
            @PathVariable Integer id,
            @RequestParam Integer empleadorId) {
        return feedbackService.verSiEsMio(id, empleadorId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('administrador')")
    public feedback.FeedbackResponse actualizar(
            @PathVariable Integer id,
            @RequestParam(required = false) Short calificacion,
            @RequestParam(required = false) String comentario,
            @RequestParam(required = false) Boolean visibleEmpleador) {
        return feedbackService.actualizar(id, calificacion, comentario, visibleEmpleador);
    }
}