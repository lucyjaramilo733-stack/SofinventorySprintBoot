package com.sofinventory.service;

import com.sofinventory.entity.Categoria;
import com.sofinventory.entity.TipoControlCategoria;
import com.sofinventory.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
/**
 * Servicio para la gestión de categorías.
 * Contiene operaciones CRUD y lógica de negocio.
 */
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Listar activas (sin fecha de eliminación)
    public List<Categoria> listarActivas() {
        return categoriaRepository.findByFechaEliminacionIsNull();
    }

    // Listar eliminadas (con fecha de eliminación)
    public List<Categoria> listarEliminadas() {
        return categoriaRepository.findByFechaEliminacionIsNotNull();
    }

    // ── Contadores para el dashboard ─────────────────────────────────────────
    public long contarActivas() {
        return categoriaRepository.countByFechaEliminacionIsNull();
    }
    // Obtener por id
    public Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // Crear categoría
    public Categoria crearCategoria(String nombre, TipoControlCategoria tipoControl,
                                    String descripcion) {
        if (categoriaRepository.existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setTipoControl(tipoControl != null ? tipoControl : TipoControlCategoria.INVENTARIO);
        categoria.setDescripcion(descripcion);
        categoria.setFechaCreacion(ZonedDateTime.now());
        categoria.setFechaActualizacion(ZonedDateTime.now());

        return categoriaRepository.save(categoria);
    }

    // Editar categoría
    public Categoria editarCategoria(Long id, String nombre,
                                     TipoControlCategoria tipoControl, String descripcion) {
        Categoria categoria = obtenerCategoria(id);

        if (categoriaRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(nombre);
        categoria.setTipoControl(tipoControl != null ? tipoControl : TipoControlCategoria.INVENTARIO);
        categoria.setDescripcion(descripcion);
        categoria.setFechaActualizacion(ZonedDateTime.now());

        return categoriaRepository.save(categoria);
    }

    // Eliminación lógica
    public void eliminarCategoria(Long id) {
        Categoria categoria = obtenerCategoria(id);
        categoria.setFechaEliminacion(ZonedDateTime.now());
        categoria.setFechaActualizacion(ZonedDateTime.now());
        categoriaRepository.save(categoria);
    }

    // Restaurar categoría
    public void restaurarCategoria(Long id) {
        Categoria categoria = obtenerCategoria(id);
        categoria.setFechaEliminacion(null);
        categoria.setFechaActualizacion(ZonedDateTime.now());
        categoriaRepository.save(categoria);
    }

    // Eliminar permanentemente
    public void eliminarPermanente(Long id) {
        categoriaRepository.deleteById(id);
    }

    // Listar todos los valores del enum para el formulario
    public TipoControlCategoria[] listarTiposControl() {
        return TipoControlCategoria.values();
    }
}
