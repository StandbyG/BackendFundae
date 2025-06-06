package com.fundae.backend.Service;

import com.fundae.backend.Exception.ResourceNotFoundException;
import com.fundae.backend.Model.AjusteRazonable;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.AjusteRazonableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AjusteRazonableService {

    private final AjusteRazonableRepository ajusteRepo;

    public List<AjusteRazonable> getAll() {
        return ajusteRepo.findAll();
    }

    public List<AjusteRazonable> getByUsuario(Usuario usuario) {
        return ajusteRepo.findByUsuario(usuario);
    }

    public AjusteRazonable getById(Integer id) {
        return ajusteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ajuste no encontrado con ID: " + id));
    }

    public AjusteRazonable save(AjusteRazonable ajuste) {
        return ajusteRepo.save(ajuste);
    }

    public void delete(Integer id) {
        if (!ajusteRepo.existsById(id)) {
            throw new ResourceNotFoundException("Ajuste no encontrado con ID: " + id);
        }
        ajusteRepo.deleteById(id);
    }
}

