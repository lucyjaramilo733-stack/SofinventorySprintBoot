package com.sofinventory.repository;

import com.sofinventory.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repositorio para la gestión de la entidad Permiso.
 * Proporciona operaciones CRUD mediante JpaRepository.
 */
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
}