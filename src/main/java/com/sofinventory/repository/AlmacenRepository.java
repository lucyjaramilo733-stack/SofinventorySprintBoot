package com.sofinventory.repository;

import com.sofinventory.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Repositorio JPA para la entidad Almacén.
 * Proporciona operaciones CRUD y consultas personalizadas
 * sobre la tabla de almacenes en la base de datos.
 */
@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    // Buscar almacenes por estado (ej. activo, inactivo)
    List<Almacen> findByEstado(String estado);

    // Verificar si existe un almacén con un código específico
    Boolean existsByCodigo(String codigo);

    // Verificar si existe un almacén con un nombre específico
    Boolean existsByNombre(String nombre);
}