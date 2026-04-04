package com.sofinventory.security;

import com.sofinventory.entity.Usuario;
import com.sofinventory.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Implementación de UserDetailsService para Spring Security.
 * Se encarga de cargar los datos del usuario desde la base de datos
 * para el proceso de autenticación.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    ///Carga un usuario por su username y lo convierte en un UserDetails.
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username
                ));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(List.of(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre())
                ))
                .accountLocked(usuario.getBloqueadoHasta() != null)
                .disabled(!usuario.getActive())
                .build();
    }
}