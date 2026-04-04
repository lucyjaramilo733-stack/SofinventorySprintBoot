package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
/**
 * Entidad JPA que representa un Producto dentro del sistema de inventario.
 * Contiene información como SKU, nombre, marca, referencia, unidad de medida,
 * estado, imagen, descripción y observaciones. Además, registra las fechas
 * de gestión y las relaciones con categoría y usuarios responsables.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    // Identificador único del producto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true, length = 60)
    private String sku;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "unidad_medida", nullable = false, length = 30)
    private String unidadMedida = "unidad";

    @Column(name = "estado", nullable = false, length = 10)
    private String estado = "activo";

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private ZonedDateTime fechaCreacion = ZonedDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

    @Column(name = "fecha_eliminacion")
    private ZonedDateTime fechaEliminacion;
    //Relaciones con: categoria y usuarios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por_id")
    private Usuario registradoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por_id")
    private Usuario actualizadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eliminado_por_id")
    private Usuario eliminadoPor;
}
