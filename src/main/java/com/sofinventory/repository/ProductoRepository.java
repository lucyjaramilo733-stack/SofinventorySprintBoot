package com.sofinventory.repository;

import com.sofinventory.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repositorio JPA para la entidad Producto.
 * Proporciona operaciones CRUD y consultas personalizadas
 * sobre la tabla de productos en la base de datos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Listar productos activos (no eliminados)
    List<Producto> findByFechaEliminacionIsNull();

    // Listar productos eliminados (soft delete)
    List<Producto> findByFechaEliminacionIsNotNull();

    // Verificar si existe un producto con un SKU específico
    Boolean existsBySku(String sku);

    // Verificar si existe un producto con un SKU específico excluyendo un id
    Boolean existsBySkuAndIdNot(String sku, Long id);
    long countByFechaEliminacionIsNull();
}
