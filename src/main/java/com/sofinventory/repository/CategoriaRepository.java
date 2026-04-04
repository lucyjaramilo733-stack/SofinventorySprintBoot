package com.sofinventory.repository;

import com.sofinventory.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Repositorio JPA para la entidad Categoria.
 * Proporciona consultas personalizadas además de las operaciones CRUD básicas.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Categorías activas (sin fecha de eliminación)
    List<Categoria> findByFechaEliminacionIsNull();
    // Categorías eliminadas (con fecha de eliminación)
    List<Categoria> findByFechaEliminacionIsNotNull();
    // Verifica si existe una categoría con el mismo nombre
    Boolean existsByNombre(String nombre);
    // Verifica si existe otra categoría con el mismo nombre (excluyendo un id)
    Boolean existsByNombreAndIdNot(String nombre, Long id);
}