package com.sofinventory.repository;

import com.sofinventory.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Capa de acceso a datos para la entidad Proveedor.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas y
 * consultas personalizadas optimizadas mediante Query Methods.
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    /**
     * Recupera una lista de proveedores filtrada por su estado operativo.
     * Utilizado principalmente para separar proveedores 'activos' de 'inactivos'
     */
    List<Proveedor> findByEstado(String estado);
    /**
     * Valida la existencia de un proveedor mediante una clave compuesta de negocio.
     * Garantiza la integridad de los datos evitando registros duplicados de un mismo
     */
    Boolean existsByNumeroDocumentoAndTipoDocumentoId(
            String numeroDocumento, Long tipoDocumentoId
    );
}