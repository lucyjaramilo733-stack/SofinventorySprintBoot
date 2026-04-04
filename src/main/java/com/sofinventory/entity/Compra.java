package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
/**
 * Entidad JPA que representa una compra a proveedores.
 * Almacena información del encabezado de la compra como número de factura,
 * fechas, totales y el estado actual de la compra.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compras")
public class Compra {
    // Identificador único
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Datos de la factura
    @Column(name = "numero_factura", nullable = false, length = 60)
    private String numeroFactura;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra = LocalDate.now();

    @Column(name = "tipo_compra", nullable = false, length = 20)
    private String tipoCompra = "contado";

    // Totales de la compr
    @Column(name = "subtotal", nullable = false, precision = 16, scale = 4)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "iva_total", nullable = false, precision = 16, scale = 4)
    private BigDecimal ivaTotal = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 16, scale = 4)
    private BigDecimal total = BigDecimal.ZERO;
    // Estado y auditoría
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "recibida";

    @Column(name = "fecha_registro", nullable = false)
    private ZonedDateTime fechaRegistro = ZonedDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

    // Relaciones-------------------------
    // Proveedor que surtió la compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;
    // Usuario que registró la compra en el sistema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private Usuario registradoPor;
    // Lista de productos comprados (detalle de la compra)
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detalles;
}
