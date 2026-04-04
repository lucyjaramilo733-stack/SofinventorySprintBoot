package com.sofinventory.repository;

import com.sofinventory.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad TipoDocumento.
 * Proporciona operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    ///Busca un tipo de documento por su código.
    Optional<TipoDocumento> findByCodigo(String codigo);
}