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

    @Column(name = "ultima_actualizacion", nullable = false)
    private ZonedDateTime ultimaActualizacion = ZonedDateTime.now();
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", nullable = false)
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
