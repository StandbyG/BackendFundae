package com.fundae.backend.auth;

import com.fundae.backend.Model.Usuario;
import com.fundae.backend.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getTipoUsuario(),usuario.getIdUsuario());
        return new AuthResponse(token, usuario.getTipoUsuario(),usuario.getIdUsuario());
    }

    public Usuario registrar(AuthRequest request) {
        Usuario usuario = new Usuario();

        usuario.setCorreo(request.getCorreo());
        usuario.setContraseñaHash(passwordEncoder.encode(request.getContraseña()));
        usuario.setNombre(request.getNombre());
        usuario.setTipoUsuario(request.getTipoUsuario());
        usuario.setNombreEmpresa(request.getNombreEmpresa());
        usuario.setRuc(request.getRuc());
        usuario.setSector(request.getSector());
        usuario.setDireccion(request.getDireccion());
        usuario.setEstadoCumplimiento(request.getEstadoCumplimiento());

        return usuarioRepository.save(usuario);
    }
}
