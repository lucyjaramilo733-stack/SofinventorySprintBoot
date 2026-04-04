package com.sofinventory.repository;

import com.sofinventory.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findAllByOrderByFechaRegistroDesc();

    List<Compra> findByProveedorId(Long proveedorId);

    Boolean existsByNumeroFacturaAndProveedorId(String numeroFactura, Long proveedorId);

    Boolean existsByNumeroFacturaAndProveedorIdAndIdNot(String numeroFactura,
                                                        Long proveedorId, Long id);
    // Método para contar por estado
    long countByEstado(String estado);

}
