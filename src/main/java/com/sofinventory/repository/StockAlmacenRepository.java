package com.sofinventory.repository;

import com.sofinventory.entity.StockAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // ── Ordenamiento ─────────────────────────────────────────────────────────

    // Ordenar todo por cantidad ascendente
    List<StockAlmacen> findAllByOrderByCantidadAsc();

    // Ordenar por almacén y luego por cantidad
    List<StockAlmacen> findByAlmacenIdOrderByCantidadAsc(Long almacenId);

    // Buscar por producto
    List<StockAlmacen> findByProductoId(Long productoId);

    // Buscar por producto y almacén específico
    Optional<StockAlmacen> findByProductoIdAndAlmacenId(Long productoId, Long almacenId);

    // Buscar por almacén (sin orden)
    List<StockAlmacen> findByAlmacenId(Long almacenId);

    // ── Consultas por cantidad ────────────────────────────────────────────────

    // Buscar por cantidad exacta (para agotados)
    List<StockAlmacen> findByCantidad(Integer cantidad);

    // Stock bajo (cantidad <= stock_minimo)
    @Query("SELECT s FROM StockAlmacen s WHERE s.cantidad <= s.stockMinimo AND s.stockMinimo IS NOT NULL")
    List<StockAlmacen> findStockBajo();

    // Stock excedido (cantidad >= stock_maximo)
    @Query("SELECT s FROM StockAlmacen s WHERE s.cantidad >= s.stockMaximo AND s.stockMaximo IS NOT NULL")
    List<StockAlmacen> findStockExcedido();
}
