package com.sofinventory.service;

import com.sofinventory.entity.Almacen;
import com.sofinventory.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
/**
 * Servicio para la gestión de almacenes.
 * Contiene la lógica de negocio para listar, crear, editar,
 * activar/inactivar y eliminar almacenes, validando reglas
 * como unicidad de código y nombre.
 */
@Service
@RequiredArgsConstructor
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    // Listar activos
    public List<Almacen> listarActivos() {
        return almacenRepository.findByEstado("activo");
    }

    // Listar inactivos
    public List<Almacen> listarInactivos() {
        return almacenRepository.findByEstado("inactivo");
    }

    // Contar activos
    public long contarActivos() {
        return almacenRepository.findByEstado("activo").size();
    }

    // Obtener por id
    public Almacen obtenerAlmacen(Long id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
    }

    // Crear almacén
    public Almacen crearAlmacen(Almacen almacen) {
        if (almacenRepository.existsByCodigo(almacen.getCodigo())) {
            throw new RuntimeException("Ya existe un almacén con ese código");
        }
        if (almacenRepository.existsByNombre(almacen.getNombre())) {
            throw new RuntimeException("Ya existe un almacén con ese nombre");
        }

        almacen.setEstado("activo");
        almacen.setFechaCreacion(ZonedDateTime.now());
        almacen.setFechaActualizacion(ZonedDateTime.now());

        return almacenRepository.save(almacen);
    }

    // Editar almacén
    public Almacen editarAlmacen(Long id, Almacen datos) {
        Almacen almacen = obtenerAlmacen(id);

        almacen.setNombre(datos.getNombre());
        almacen.setDireccion(datos.getDireccion());
        almacen.setResponsable(datos.getResponsable());
        almacen.setTelefono(datos.getTelefono());
        almacen.setCapacidad(datos.getCapacidad());
        almacen.setNotas(datos.getNotas());
        almacen.setFechaActualizacion(ZonedDateTime.now());

        return almacenRepository.save(almacen);
    }

    // Desactivar almacén
    public void desactivarAlmacen(Long id) {
        Almacen almacen = obtenerAlmacen(id);
        almacen.setEstado("inactivo");
        almacen.setFechaActualizacion(ZonedDateTime.now());
        almacenRepository.save(almacen);
    }

    // Reactivar almacén
    public void reactivarAlmacen(Long id) {
        Almacen almacen = obtenerAlmacen(id);
        almacen.setEstado("activo");
        almacen.setFechaActualizacion(ZonedDateTime.now());
        almacenRepository.save(almacen);
    }

    // Eliminar permanentemente
    public void eliminarAlmacen(Long id) {
        almacenRepository.deleteById(id);
    }
}