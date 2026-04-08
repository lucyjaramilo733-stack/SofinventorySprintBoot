package com.sofinventory.repository;

import com.sofinventory.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Gestión de persistencia para los ítems de cada compra.
 * Muestra al detalle por cada producto comprado
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    // Recupera todos los artículos que pertenecen a una sola factura
    List<DetalleCompra> findByCompraId(Long compraId);
    // Permite rastrear en qué compras se ha adquirido un producto específico
    List<DetalleCompra> findByProductoId(Long productoId);
}
