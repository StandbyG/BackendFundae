package com.fundae.backend.Service;


import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Model.Verificacion;
import com.fundae.backend.Repository.VerificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificacionService {

    private final VerificacionRepository verificacionRepo;

    public List<Verificacion> getAll() {
        return verificacionRepo.findAll();
    }

    public List<Verificacion> getByUsuario(Usuario usuario) {
        return verificacionRepo.findByUsuario(usuario);
    }

    public Verificacion getById(Integer id) {
        return verificacionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verificación no encontrada con ID: " + id));
    }

    public Verificacion save(Verificacion v) {
        return verificacionRepo.save(v);
    }

    public void delete(Integer id) {
        if (!verificacionRepo.existsById(id)) {
            throw new ResourceNotFoundException("Verificación no encontrada con ID: " + id);
        }
        verificacionRepo.deleteById(id);
    }
}
