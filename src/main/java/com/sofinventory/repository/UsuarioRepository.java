package com.sofinventory.repository;

import com.sofinventory.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad Usuario.
 * Proporciona operaciones CRUD y consultas para autenticación y validación.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    ///Busca un usuario por su nombre de usuario.
    Optional<Usuario> findByUsername(String username);
    ///Verifica si existe un usuario con el username dado.
    Optional<Usuario> findByEmail(String email);
    Boolean existsByUsername(String username);
    ///Verifica si existe un usuario con el email dado.
    Boolean existsByEmail(String email);
}