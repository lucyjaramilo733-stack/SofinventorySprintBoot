package com.sofinventory.repository;

import com.sofinventory.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de la entidad RolPermiso.
 * Proporciona operaciones CRUD mediante JpaRepository.
 */
@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Long> {
}