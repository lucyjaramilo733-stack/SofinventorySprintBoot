package com.sofinventory.repository;

import com.sofinventory.entity.StockAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad StockAlmacen.
 * Proporciona operaciones CRUD y consultas personalizadas
 * sobre el stock de productos en los almacenes.
 */
@Repository
public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Long> {
    // Buscar stock por producto y almacén específico
    Optional<StockAlmacen> findByProductoIdAndAlmacenId(Long productoId, Long almacenId);
    // Listar stock por producto
    List<StockAlmacen> findByProductoId(Long productoId);
    // Listar stock por almacén
    List<StockAlmacen> findByAlmacenId(Long almacenId);
}
