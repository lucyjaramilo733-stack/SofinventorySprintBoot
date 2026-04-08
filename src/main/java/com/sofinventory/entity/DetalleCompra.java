package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * Entidad que representa cada ítem o producto individual dentro de una compra.
 * Permite desglosar cantidades, costos unitarios e impuestos por producto.
 * Implementa la lógica detallada del Caso de Uso: "Registrar ítems de compra".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_compras")
public class DetalleCompra {
    // Identificador único autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 14, scale = 4)
    private BigDecimal costoUnitario;

    @Column(name = "iva_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal ivaPorcentaje = BigDecimal.ZERO;

    @Column(name = "subtotal", nullable = false, precision = 16, scale = 4)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 16, scale = 4)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "codigo_lote", length = 60)
    private String codigoLote;

    // Relaciones
    // Atributos específicos para control de inventario en ferreterías
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", nullable = false)
    private Almacen almacen;
}
