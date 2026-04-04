package com.sofinventory.repository;

import com.sofinventory.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEstado(String estado);

    Boolean existsByNumeroDocumentoAndTipoDocumentoId(
            String numeroDocumento, Long tipoDocumentoId
    );
}