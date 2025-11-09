package com.fundae.backend.Service;


import com.fundae.backend.Exception.EmailInUseException;
import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.UsuarioRepository;
import com.fundae.backend.dto.UsuarioUpdateDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    public Usuario getUsuarioOrThrow(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario updatePerfil(Integer id, UsuarioUpdateDTO dto) {
        Usuario u = getUsuarioOrThrow(id);

        java.util.function.Function<String,String> norm =
                s -> s == null ? null : s.trim();

        if (dto.getCorreo() != null) {
            String nuevoCorreo = norm.apply(dto.getCorreo()).toLowerCase();
            String correoActual = u.getCorreo() == null ? null : u.getCorreo().trim().toLowerCase();

            if (!nuevoCorreo.equals(correoActual)) {
                boolean enUso = usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(nuevoCorreo, u.getIdUsuario());
                if (enUso) {
                    throw new EmailInUseException("El correo ya está en uso.");
                }
                u.setCorreo(nuevoCorreo);
            }
        }

        if (dto.getNombre() != null) u.setNombre(dto.getNombre());
        if (dto.getTipoUsuario() != null) u.setTipoUsuario(dto.getTipoUsuario());
        if (dto.getNombreEmpresa() != null) u.setNombreEmpresa(dto.getNombreEmpresa());
        if (dto.getRuc() != null) u.setRuc(dto.getRuc());
        if (dto.getSector() != null) u.setSector(dto.getSector());
        if (dto.getDireccion() != null) u.setDireccion(dto.getDireccion());
        if (dto.getEstadoCumplimiento() != null) u.setEstadoCumplimiento(dto.getEstadoCumplimiento());

        try {
            return usuarioRepository.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new EmailInUseException("El correo ya está en uso.");
        }
    }
}

