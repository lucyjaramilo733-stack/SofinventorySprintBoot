package com.sofinventory.service;

import com.sofinventory.dto.LoginRequest;
import com.sofinventory.dto.LoginResponse;
import com.sofinventory.entity.Usuario;
import com.sofinventory.repository.UsuarioRepository;
import com.sofinventory.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
/**
 * Servicio encargado del proceso de autenticación de usuarios.
 * Valida credenciales y genera el token JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository
                .findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtUtils.generateToken(usuario.getUsername());

        return new LoginResponse(
                token,
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getRol().getNombre(),
                "Login exitoso"
        );
    }
}