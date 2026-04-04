package com.sofinventory.service;

import com.sofinventory.entity.Categoria;
import com.sofinventory.entity.Producto;
import com.sofinventory.entity.Usuario;
import com.sofinventory.repository.CategoriaRepository;
import com.sofinventory.repository.ProductoRepository;
import com.sofinventory.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
/**
 * Servicio para la gestión de productos.
 * Contiene la lógica de negocio para listar, crear, editar,
 * eliminar (lógica y permanente) y restaurar productos.
 * Valida reglas como la unicidad del SKU y la existencia de
 * categorías y usuarios relacionados.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    // Listar productos activos
    public List<Producto> listarActivos() {
        return productoRepository.findByFechaEliminacionIsNull();
    }

    // Listar productos eliminados
    public List<Producto> listarEliminados() {
        return productoRepository.findByFechaEliminacionIsNotNull();
    }
    // Obtener producto por id

    // ── Contadores para el dashboard ─────────────────────────────────────────
    public long contarActivos() {
        return productoRepository.countByFechaEliminacionIsNull();
    }
    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Crear nuevo producto
    public Producto crearProducto(String sku, String nombre, String marca,
                                  String referencia, String unidadMedida,
                                  String usernameRegistra, String descripcion,
                                  String observaciones, Long categoriaId) {

        if (productoRepository.existsBySku(sku)) {
            throw new RuntimeException("Ya existe un producto con ese SKU");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Usuario registradoPor = usuarioRepository.findByUsername(usernameRegistra)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = new Producto();
        producto.setSku(sku);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setReferencia(referencia);
        producto.setUnidadMedida(unidadMedida != null ? unidadMedida : "unidad");
        producto.setRegistradoPor(registradoPor);
        producto.setDescripcion(descripcion);
        producto.setObservaciones(observaciones);
        producto.setEstado("activo");
        producto.setCategoria(categoria);
        producto.setFechaCreacion(ZonedDateTime.now());
        producto.setFechaActualizacion(ZonedDateTime.now());

        return productoRepository.save(producto);
    }

    // Editar producto existente
    public Producto editarProducto(Long id, String sku, String nombre, String marca,
                                   String referencia, String unidadMedida, String descripcion,
                                   String observaciones, Long categoriaId) {

        Producto producto = obtenerProducto(id);

        if (productoRepository.existsBySkuAndIdNot(sku, id)) {
            throw new RuntimeException("Ya existe un producto con ese SKU");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setSku(sku);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setReferencia(referencia);
        producto.setUnidadMedida(unidadMedida != null ? unidadMedida : "unidad");
        producto.setDescripcion(descripcion);
        producto.setObservaciones(observaciones);
        producto.setCategoria(categoria);
        producto.setFechaActualizacion(ZonedDateTime.now());

        return productoRepository.save(producto);
    }

    // Eliminación lógica de producto
    public void eliminarProducto(Long id) {
        Producto producto = obtenerProducto(id);
        producto.setFechaEliminacion(ZonedDateTime.now());
        producto.setFechaActualizacion(ZonedDateTime.now());
        productoRepository.save(producto);
    }

    // Restaurar producto eliminado
    public void restaurarProducto(Long id) {
        Producto producto = obtenerProducto(id);
        producto.setFechaEliminacion(null);
        producto.setFechaActualizacion(ZonedDateTime.now());
        productoRepository.save(producto);
    }
    // Eliminación permanente de producto
    public void eliminarPermanente(Long id) {
        productoRepository.deleteById(id);
    }
    // Listar categorías activas
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByFechaEliminacionIsNull();
    }
}

