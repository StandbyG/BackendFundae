package com.fundae.backend.Service;

import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.OrigenAjuste;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.dto.AjusteEstadoUpdateDTO;
import com.fundae.backend.dto.AjusteRazonableCreateDTO;
import com.fundae.backend.dto.AjusteRazonableOngCreateDTO;
import com.fundae.backend.dto.AjusteRazonableResponseDTO;
import com.fundae.backend.dto.AjusteRazonableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AjusteRazonableService {

    private final AjusteRazonableRepository ajusteRepo;
    private final UsuarioService usuarioService;

    public AjusteRazonableResponseDTO updateEstadoYFecha(Integer id, AjusteEstadoUpdateDTO dto) {
        AjusteRazonable ajusteExistente = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));
        ajusteExistente.setEstado(dto.getEstado());
        ajusteExistente.setFechaImplementacion(dto.getFechaImplementacion());
        AjusteRazonable ajusteGuardado = ajusteRepo.save(ajusteExistente);
        return AjusteRazonableMapper.toResponse(ajusteGuardado);
    }

    public List<AjusteRazonable> saveBulk(List<AjusteRazonableCreateDTO> ajustesDTO) {
        if (ajustesDTO == null || ajustesDTO.isEmpty()) return Collections.emptyList();
        Integer usuarioId = ajustesDTO.get(0).getUsuarioId();
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        List<AjusteRazonable> nuevosAjustes = ajustesDTO.stream().map(dto -> {
            AjusteRazonable ajuste = new AjusteRazonable();
            ajuste.setTipoAjuste(dto.getTipoAjuste());
            ajuste.setDescripcion(dto.getDescripcion());
            ajuste.setEstado(dto.getEstado());
            ajuste.setFechaRecomendacion(dto.getFechaRecomendacion());
            ajuste.setFechaImplementacion(dto.getFechaImplementacion());
            ajuste.setUsuario(usuario);
            return ajuste;
        }).collect(Collectors.toList());
        return ajusteRepo.saveAll(nuevosAjustes);
    }

    public List<AjusteRazonableResponseDTO> getAll() {
        return ajusteRepo.findAll().stream()
                .map(AjusteRazonableMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AjusteRazonableResponseDTO> getByUsuarioId(Integer idUsuario) {
        return ajusteRepo.findByUsuario_IdUsuario(idUsuario).stream()
                .map(AjusteRazonableMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AjusteRazonableResponseDTO getById(Integer id) {
        AjusteRazonable ajuste = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));
        return AjusteRazonableMapper.toResponse(ajuste);
    }

    public AjusteRazonable save(AjusteRazonable ajuste) {
        return ajusteRepo.save(ajuste);
    }

    public void delete(Integer id) {
        AjusteRazonable ajusteExistente = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));
        ajusteRepo.deleteById(ajusteExistente.getIdAjuste());
    }

    public List<AjusteRazonable> getByEstado(String estado) {
        return ajusteRepo.findByEstado(estado);
    }

    public AjusteRazonable crearDesdeOng(AjusteRazonableOngCreateDTO dto) {
        Usuario usuario = usuarioService.getUsuarioById(dto.getUsuarioId());
        AjusteRazonable ar = new AjusteRazonable();
        ar.setUsuario(usuario);
        ar.setOrigen(OrigenAjuste.ONG);
        ar.setEspacio(dto.getEspacio());
        ar.setObservacion(dto.getObservacion());
        ar.setAjustesSugeridos(dto.getAjustesSugeridos());
        ar.setRefNormativa(dto.getRefNormativa());
        ar.setRefFotografica(dto.getRefFotografica());
        ar.setDificultad(dto.getDificultad());
        ar.setUrgencia(dto.getUrgencia());
        ar.setEstado(Objects.requireNonNullElse(dto.getEstado(), "pendiente"));
        if (dto.getFechaRecomendacion() != null) ar.setFechaRecomendacion(LocalDate.parse(dto.getFechaRecomendacion()));
        if (dto.getFechaImplementacion() != null) ar.setFechaImplementacion(LocalDate.parse(dto.getFechaImplementacion()));
        return ajusteRepo.save(ar);
    }
}
