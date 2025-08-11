package com.fundae.backend.Service;

import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import com.fundae.backend.dto.AjusteEstadoUpdateDTO;
import com.fundae.backend.dto.AjusteRazonableResponseDTO;
import com.fundae.backend.dto.AjusteRazonableUpdateDTO;
import com.fundae.backend.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AjusteRazonableService {

    private final AjusteRazonableRepository ajusteRepo;

    public AjusteRazonableResponseDTO updateEstadoYFecha(Integer id, AjusteEstadoUpdateDTO dto) {
        // 1. Obtenemos la ENTIDAD real de la base de datos
        AjusteRazonable ajusteExistente = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));


        // 3. Actualizamos los campos permitidos
        ajusteExistente.setEstado(dto.getEstado());
        ajusteExistente.setFechaImplementacion(dto.getFechaImplementacion());

        // 4. Guardamos la entidad y devolvemos el DTO actualizado
        AjusteRazonable ajusteGuardado = ajusteRepo.save(ajusteExistente);
        return convertToDto(ajusteGuardado);
    }
    private AjusteRazonableResponseDTO convertToDto(AjusteRazonable ajuste) {
        AjusteRazonableResponseDTO dto = new AjusteRazonableResponseDTO();
        dto.setIdAjuste(ajuste.getIdAjuste());
        dto.setTipoAjuste(ajuste.getTipoAjuste());
        dto.setDescripcion(ajuste.getDescripcion());
        dto.setEstado(ajuste.getEstado());
        dto.setFechaRecomendacion(ajuste.getFechaRecomendacion());
        dto.setFechaImplementacion(ajuste.getFechaImplementacion());
        dto.setAlertado(ajuste.isAlertado());

        // Mapear el usuario a su DTO
        if (ajuste.getUsuario() != null) {
            UsuarioDTO usuarioDto = new UsuarioDTO();
            usuarioDto.setIdUsuario(ajuste.getUsuario().getIdUsuario());
            usuarioDto.setNombre(ajuste.getUsuario().getNombre());
            usuarioDto.setCorreo(ajuste.getUsuario().getCorreo());
            usuarioDto.setNombreEmpresa(ajuste.getUsuario().getNombreEmpresa());
            dto.setUsuario(usuarioDto);
        }
        return dto;
    }
    public List<AjusteRazonableResponseDTO> getAll() {
        return ajusteRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<AjusteRazonableResponseDTO> getByUsuarioId(Integer idUsuario) {
        return ajusteRepo.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AjusteRazonableResponseDTO getById(Integer id) {
        AjusteRazonable ajuste = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));

        // Convierte la entidad encontrada a un DTO antes de devolverla
        return convertToDto(ajuste);
    }
    public AjusteRazonable save(AjusteRazonable ajuste) {
        return ajusteRepo.save(ajuste);
    }

    public void delete(Integer id) {
        AjusteRazonable ajusteExistente = ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));

        ajusteRepo.deleteById(id);
    }
    public List<AjusteRazonable> getByEstado(String estado) {
        return ajusteRepo.findByEstado(estado);
    }

}

