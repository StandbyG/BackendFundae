package com.fundae.backend.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Correo no registrado: " + correo));

        System.out.println("Rol del usuario desde la BD: '" + usuario.getTipoUsuario() + "'");

        return new User(usuario.getCorreo(), usuario.getContraseñaHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().toUpperCase())));
    }
}
