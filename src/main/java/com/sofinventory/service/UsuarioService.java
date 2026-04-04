package com.sofinventory.service;

import com.sofinventory.entity.Role;
import com.sofinventory.entity.TipoDocumento;
import com.sofinventory.entity.Usuario;
import com.sofinventory.repository.RoleRepository;
import com.sofinventory.repository.TipoDocumentoRepository;
import com.sofinventory.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Servicio encargado de la gestión de usuarios.
 * Maneja operaciones CRUD, validaciones y lógica de negocio.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PasswordEncoder passwordEncoder;

    // Listar usuarios activos
    public List<Usuario> listarActivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getActive())
                .toList();
    }

    // Listar usuarios inactivos
    public List<Usuario> listarInactivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getActive())
                .toList();
    }

    // Contar activos
    public long contarActivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getActive())
                .count();
    }

    // Contar inactivos
    public long contarInactivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getActive())
                .count();
    }

    // Crear nuevo usuario
    public Usuario crearUsuario(Usuario usuario, Long rolId,
                                Long tipoDocumentoId, String password) {
        Role rol = roleRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo documento no encontrado"));

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        usuario.setRol(rol);
        usuario.setTipoDocumento(tipoDoc);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActive(true);

        return usuarioRepository.save(usuario);
    }

    // Obtener usuario por id
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Editar usuario
    public Usuario editarUsuario(Long id, String nombreCompleto,
                                 String email, Long rolId,
                                 Long tipoDocumentoId) {
        Usuario usuario = obtenerUsuario(id);

        Role rol = roleRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo documento no encontrado"));

        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setTipoDocumento(tipoDoc);

        return usuarioRepository.save(usuario);
    }

    // Desactivar usuario
    public void desactivarUsuario(Long id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.setActive(false);
        usuarioRepository.save(usuario);
    }

    // Reactivar usuario
    public void reactivarUsuario(Long id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.setActive(true);
        usuarioRepository.save(usuario);
    }

    // Eliminar permanentemente
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Listar roles
    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }

    // Listar tipos de documento
    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }
}