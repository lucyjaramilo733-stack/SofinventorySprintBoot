package com.sofinventory.repository;

import com.sofinventory.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad Role.
 * Proporciona operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    //Busca un rol por su nombre.
    Optional<Role> findByNombre(String nombre);
}