package com.sofinventory.service;

import com.sofinventory.entity.Proveedor;
import com.sofinventory.entity.TipoDocumento;
import com.sofinventory.repository.ProveedorRepository;
import com.sofinventory.repository.TipoDocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    // Listar activos
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEstado("activo");
    }

    // Listar inactivos
    public List<Proveedor> listarInactivos() {
        return proveedorRepository.findByEstado("inactivo");
    }

    // Contar activos
    public long contarActivos() {
        return proveedorRepository.findByEstado("activo").size();
    }

    // Obtener por id
    public Proveedor obtenerProveedor(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    // Crear proveedor
    public Proveedor crearProveedor(Proveedor proveedor, Long tipoDocumentoId) {
        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo documento no encontrado"));

        if (proveedorRepository.existsByNumeroDocumentoAndTipoDocumentoId(
                proveedor.getNumeroDocumento(), tipoDocumentoId)) {
            throw new RuntimeException("Ya existe un proveedor con ese documento");
        }

        proveedor.setTipoDocumento(tipoDoc);
        proveedor.setEstado("activo");
        proveedor.setFechaRegistro(ZonedDateTime.now());
        proveedor.setFechaActualizacion(ZonedDateTime.now());

        return proveedorRepository.save(proveedor);
    }

    // Editar proveedor
    public Proveedor editarProveedor(Long id, Proveedor datos, Long tipoDocumentoId) {
        Proveedor proveedor = obtenerProveedor(id);
        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo documento no encontrado"));

        proveedor.setRazonSocial(datos.getRazonSocial());
        proveedor.setNombreComercial(datos.getNombreComercial());
        proveedor.setNombreContacto(datos.getNombreContacto());
        proveedor.setCargoContacto(datos.getCargoContacto());
        proveedor.setEmail(datos.getEmail());
        proveedor.setTelefono(datos.getTelefono());
        proveedor.setDireccion(datos.getDireccion());
        proveedor.setPais(datos.getPais());
        proveedor.setDepartamento(datos.getDepartamento());
        proveedor.setCiudad(datos.getCiudad());
        proveedor.setTipoProveedor(datos.getTipoProveedor());
        proveedor.setObservaciones(datos.getObservaciones());
        proveedor.setTipoDocumento(tipoDoc);
        proveedor.setFechaActualizacion(ZonedDateTime.now());

        return proveedorRepository.save(proveedor);
    }

    // Desactivar proveedor
    public void desactivarProveedor(Long id) {
        Proveedor proveedor = obtenerProveedor(id);
        proveedor.setEstado("inactivo");
        proveedor.setFechaActualizacion(ZonedDateTime.now());
        proveedorRepository.save(proveedor);
    }

    // Reactivar proveedor
    public void reactivarProveedor(Long id) {
        Proveedor proveedor = obtenerProveedor(id);
        proveedor.setEstado("activo");
        proveedor.setFechaActualizacion(ZonedDateTime.now());
        proveedorRepository.save(proveedor);
    }

    // Eliminar permanentemente
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    // Listar tipos de documento
    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }
}
