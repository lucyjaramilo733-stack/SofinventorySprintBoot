package com.sofinventory.service;

import com.sofinventory.entity.*;
import com.sofinventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Servicio para la gestión de compras a proveedores.
 * Maneja el registro de compras, cálculo de totales, actualización de stock
 * y anulación de compras con reversión de inventario.
 */
@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository        compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProveedorRepository     proveedorRepository;
    private final ProductoRepository      productoRepository;
    private final AlmacenRepository       almacenRepository;
    private final StockAlmacenRepository  stockAlmacenRepository;
    private final UsuarioRepository       usuarioRepository;

    // ── Listar todas las compras ──────────────────────────────────────────────
    public List<Compra> listarCompras() {
        return compraRepository.findAllByOrderByFechaRegistroDesc();
    }

    // ── Obtener compra por id ─────────────────────────────────────────────────
    public Compra obtenerCompra(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    // ── Obtener detalles de una compra ────────────────────────────────────────
    public List<DetalleCompra> obtenerDetalles(Long compraId) {
        return detalleCompraRepository.findByCompraId(compraId);
    }

    // ── Registrar nueva compra ────────────────────────────────────────────────
    @Transactional
    public Compra registrarCompra(String numeroFactura,
                                  LocalDate fechaCompra,
                                  String tipoCompra,
                                  Long proveedorId,
                                  String usernameRegistra,
                                  List<Long> productosIds,
                                  List<Long> almacenesIds,
                                  List<Integer> cantidades,
                                  List<BigDecimal> costosUnitarios,
                                  List<BigDecimal> ivasPorcentaje,
                                  List<String> codigosLote,
                                  List<LocalDate> fechasVencimiento) {

        // Validar factura única por proveedor
        if (compraRepository.existsByNumeroFacturaAndProveedorId(numeroFactura, proveedorId)) {
            throw new RuntimeException(
                    "Ya existe una compra con ese número de factura para este proveedor");
        }

        // Cargar proveedor y usuario
        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Usuario registradoPor = usuarioRepository.findByUsername(usernameRegistra)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear cabecera de compra
        Compra compra = new Compra();
        compra.setNumeroFactura(numeroFactura);
        compra.setFechaCompra(fechaCompra != null ? fechaCompra : LocalDate.now());
        compra.setTipoCompra(tipoCompra != null ? tipoCompra : "contado");
        compra.setEstado("recibida");
        compra.setProveedor(proveedor);
        compra.setRegistradoPor(registradoPor);
        compra.setFechaRegistro(ZonedDateTime.now());
        compra.setFechaActualizacion(ZonedDateTime.now());

        // Construir detalles y calcular totales
        List<DetalleCompra> detalles = new ArrayList<>();
        BigDecimal subtotalCompra = BigDecimal.ZERO;
        BigDecimal ivaTotalCompra = BigDecimal.ZERO;

        for (int i = 0; i < productosIds.size(); i++) {

            Producto producto = productoRepository.findById(productosIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Almacen almacen = almacenRepository.findById(almacenesIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            Integer cantidad      = cantidades.get(i);
            BigDecimal costo      = costosUnitarios.get(i);
            BigDecimal ivaPct     = ivasPorcentaje.get(i) != null
                    ? ivasPorcentaje.get(i)
                    : BigDecimal.ZERO;

            // subtotal línea = cantidad × costo
            BigDecimal subtotalLinea = costo
                    .multiply(BigDecimal.valueOf(cantidad))
                    .setScale(4, RoundingMode.HALF_UP);

            // iva línea = subtotal × (iva% / 100)
            BigDecimal ivaLinea = subtotalLinea
                    .multiply(ivaPct)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            // total línea = subtotal + iva
            BigDecimal totalLinea = subtotalLinea.add(ivaLinea);

            // Acumular en la cabecera
            subtotalCompra = subtotalCompra.add(subtotalLinea);
            ivaTotalCompra = ivaTotalCompra.add(ivaLinea);

            // Construir detalle
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCantidad(cantidad);
            detalle.setCostoUnitario(costo);
            detalle.setIvaPorcentaje(ivaPct);
            detalle.setSubtotal(subtotalLinea);
            detalle.setTotal(totalLinea);
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setAlmacen(almacen);

            // Lote y vencimiento (opcionales)
            if (codigosLote != null && i < codigosLote.size()) {
                detalle.setCodigoLote(codigosLote.get(i));
            }
            if (fechasVencimiento != null && i < fechasVencimiento.size()) {
                detalle.setFechaVencimiento(fechasVencimiento.get(i));
            }

            detalles.add(detalle);

            // Actualizar StockAlmacen
            actualizarStock(producto, almacen, cantidad);
        }

        // Asignar totales a la cabecera
        compra.setSubtotal(subtotalCompra);
        compra.setIvaTotal(ivaTotalCompra);
        compra.setTotal(subtotalCompra.add(ivaTotalCompra));
        compra.setDetalles(detalles);

        return compraRepository.save(compra);
    }

    // ── Anular compra ─────────────────────────────────────────────────────────
    @Transactional
    public void anularCompra(Long id) {
        Compra compra = obtenerCompra(id);

        if (compra.getEstado().equals("anulada")) {
            throw new RuntimeException("La compra ya está anulada");
        }

        // Revertir stock por cada detalle
        for (DetalleCompra detalle : compra.getDetalles()) {
            actualizarStock(detalle.getProducto(), detalle.getAlmacen(),
                    -detalle.getCantidad());
        }

        compra.setEstado("anulada");
        compra.setFechaActualizacion(ZonedDateTime.now());
        compraRepository.save(compra);
    }

    // ── Actualizar StockAlmacen ───────────────────────────────────────────────
    private void actualizarStock(Producto producto, Almacen almacen, Integer cantidad) {
        StockAlmacen stock = stockAlmacenRepository
                .findByProductoIdAndAlmacenId(producto.getId(), almacen.getId())
                .orElseGet(() -> {
                    // Si no existe el registro, lo crea con 0
                    StockAlmacen nuevo = new StockAlmacen();
                    nuevo.setProducto(producto);
                    nuevo.setAlmacen(almacen);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        int nuevaCantidad = stock.getCantidad() + cantidad;
        if (nuevaCantidad < 0) {
            throw new RuntimeException("Stock insuficiente para el producto: "
                    + producto.getNombre());
        }

        stock.setCantidad(nuevaCantidad);
        stock.setUltimaActualizacion(ZonedDateTime.now());
        stockAlmacenRepository.save(stock);
    }

    // ── Datos para formularios ────────────────────────────────────────────────
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findByEstado("activo");
    }

    public List<Producto> listarProductos() {
        return productoRepository.findByFechaEliminacionIsNull();
    }

    public List<Almacen> listarAlmacenes() {
        return almacenRepository.findByEstado("activo");
    }
    // ── Contadores para el dashboard ──────────────────────────────────────────────
    public long contarTotalCompras() {
        return compraRepository.count();
    }
}
