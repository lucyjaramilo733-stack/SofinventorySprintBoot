package com.sofinventory.repository;

import com.sofinventory.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;/**
 * Repositorio JPA para la entidad Compras.
 * Proporciona operaciones CRUD y consultas personalizadas
 * sobre la tabla de compras en la base de datos.
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    // Recupera compras ordenadas por la más reciente
    List<Compra> findAllByOrderByFechaRegistroDesc();
    // Filtra compras por un proveedor específico
    List<Compra> findByProveedorId(Long proveedorId);
    // Valida si ya existe una factura para un proveedor
    Boolean existsByNumeroFacturaAndProveedorId(String numeroFactura, Long proveedorId);
    // Valida duplicados excluyendo el ID actual (Útil para actualizaciones/edición)
    Boolean existsByNumeroFacturaAndProveedorIdAndIdNot(String numeroFactura,
                                                        Long proveedorId, Long id);

    // Método para contar por estado
    long countByEstado(String estado);

}
