package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Entidad JPA que representa el stock de un producto dentro de un almacén.
 * Permite llevar control de la cantidad disponible, la última fecha de actualización
 * y las relaciones con las entidades Almacén y Producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_almacen")
public class StockAlmacen {

    // Identificador único del registro de stock
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 0;

    // ✅ NUEVOS CAMPOS - AGREGAR ESTOS
    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    @Column(name = "ultima_actualizacion", nullable = false)
    private ZonedDateTime ultimaActualizacion = ZonedDateTime.now();

    // Relaciones con: almacenes y producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", nullable = false)
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;


    // ── Métodos auxiliares para la vista (Thymeleaf) ──────────────────────────

    /**
     * Indica si el producto está agotado (cantidad = 0)
     */
    public boolean isAgotado() {
        return cantidad != null && cantidad == 0;
    }

    /**
     * Indica si el producto tiene stock bajo (cantidad <= stock mínimo y > 0)
     */
    public boolean isStockBajo() {
        return stockMinimo != null
                && cantidad != null
                && cantidad <= stockMinimo
                && cantidad > 0;
    }

    /**
     * Indica si el producto tiene stock excedido (cantidad >= stock máximo)
     */
    public boolean isStockExcedido() {
        return stockMaximo != null
                && cantidad != null
                && cantidad >= stockMaximo;
    }

    // Getter manual para que Thymeleaf pueda acceder como propiedad
    public boolean getAgotado() {
        return isAgotado();
    }

    public boolean getStockBajo() {
        return isStockBajo();
    }

    public boolean getStockExcedido() {
        return isStockExcedido();
    }

}