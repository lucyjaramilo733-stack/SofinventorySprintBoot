package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
/**
 * Entidad JPA que representa un Almacén dentro del sistema de inventario.
 * Contiene información administrativa y de control como nombre, código,
 * dirección, responsable, capacidad, estado y fechas de gestión.
 * Se relaciona con la entidad Usuario para registrar quién creó, actualizó
 * o eliminó el almacén.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "almacenes")
public class Almacen {
    // Identificador único del almacén
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo", nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "responsable", length = 120)
    private String responsable;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "capacidad", precision = 12, scale = 2)
    private BigDecimal capacidad;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado = "activo";

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_creacion", nullable = false)
    private ZonedDateTime fechaCreacion = ZonedDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

    @Column(name = "fecha_eliminacion")
    private ZonedDateTime fechaEliminacion;
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id")
    private Usuario creadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por_id")
    private Usuario actualizadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eliminado_por_id")
    private Usuario eliminadoPor;
}