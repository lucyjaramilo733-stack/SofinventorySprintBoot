package com.sofinventory.service;

import com.sofinventory.entity.Almacen;
import com.sofinventory.entity.Producto;
import com.sofinventory.entity.StockAlmacen;
import com.sofinventory.repository.AlmacenRepository;
import com.sofinventory.repository.ProductoRepository;
import com.sofinventory.repository.StockAlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
/**
 * Servicio para la gestión del stock en almacenes.
 * Maneja consultas de inventario, alertas de stock bajo/agotado,
 * ajustes manuales de cantidad y configuración de umbrales mínimo/máximo.
 */
@Service
@RequiredArgsConstructor
public class StockAlmacenService {

    private final StockAlmacenRepository stockAlmacenRepository;
    private final AlmacenRepository      almacenRepository;
    private final ProductoRepository     productoRepository;

    // ── Consultas generales ───────────────────────────────────────────────────

    public List<StockAlmacen> listarTodo() {
        return stockAlmacenRepository.findAllByOrderByCantidadAsc();
    }

    public List<StockAlmacen> listarPorAlmacen(Long almacenId) {
        return stockAlmacenRepository.findByAlmacenIdOrderByCantidadAsc(almacenId);
    }

    public List<StockAlmacen> listarPorProducto(Long productoId) {
        return stockAlmacenRepository.findByProductoId(productoId);
    }

    public StockAlmacen obtenerPorId(Long id) {
        return stockAlmacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de stock no encontrado"));
    }

    // ── Consultas de alertas ──────────────────────────────────────────────────

    public List<StockAlmacen> listarAgotados() {
        return stockAlmacenRepository.findByCantidad(0);
    }

    public List<StockAlmacen> listarStockBajo() {
        return stockAlmacenRepository.findStockBajo();
    }

    public List<StockAlmacen> listarStockExcedido() {
        return stockAlmacenRepository.findStockExcedido();
    }

    // ── Contadores para el dashboard ─────────────────────────────────────────

    public long contarAgotados() {
        return stockAlmacenRepository.findByCantidad(0).size();
    }

    public long contarStockBajo() {
        return stockAlmacenRepository.findStockBajo().size();
    }

    // ── Ajuste manual de stock ────────────────────────────────────────────────

    @Transactional
    public void ajustarStock(Long stockId, Integer cantidad,
                             String tipoAjuste, String motivo) {

        StockAlmacen stock = obtenerPorId(stockId);

        int delta = tipoAjuste.equals("entrada") ? cantidad : -cantidad;
        int nuevaCantidad = stock.getCantidad() + delta;

        if (nuevaCantidad < 0) {
            throw new RuntimeException(
                    "Stock insuficiente. Disponible: " + stock.getCantidad()
                            + ", intentó restar: " + cantidad);
        }

        // Advertir si supera el máximo pero permitir la operación
        if (stock.getStockMaximo() != null && nuevaCantidad > stock.getStockMaximo()) {
            throw new RuntimeException(
                    "La cantidad resultante (" + nuevaCantidad
                            + ") supera el stock máximo configurado ("
                            + stock.getStockMaximo() + "). Ajusta el máximo o reduce la cantidad.");
        }

        stock.setCantidad(nuevaCantidad);
        stock.setUltimaActualizacion(ZonedDateTime.now());
        stockAlmacenRepository.save(stock);
    }

    // ── Actualizar umbrales mínimo y máximo ───────────────────────────────────

    @Transactional
    public void actualizarUmbrales(Long stockId, Integer stockMinimo,
                                   Integer stockMaximo) {

        StockAlmacen stock = obtenerPorId(stockId);

        if (stockMinimo < 0) {
            throw new RuntimeException("El stock mínimo no puede ser negativo.");
        }

        if (stockMaximo != null && stockMaximo < stockMinimo) {
            throw new RuntimeException(
                    "El stock máximo (" + stockMaximo
                            + ") no puede ser menor que el mínimo (" + stockMinimo + ").");
        }

        stock.setStockMinimo(stockMinimo);
        stock.setStockMaximo(stockMaximo);
        stock.setUltimaActualizacion(ZonedDateTime.now());
        stockAlmacenRepository.save(stock);
    }

    // ── Datos auxiliares para formularios ─────────────────────────────────────

    public List<Almacen> listarAlmacenes() {
        return almacenRepository.findByEstado("activo");
    }

    public List<Producto> listarProductos() {
        return productoRepository.findByFechaEliminacionIsNull();
    }
}