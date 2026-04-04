package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
/**
 * Entidad JPA que representa una Categoría dentro del sistema de inventario.
 * Define los atributos principales de una categoría, incluyendo su nombre,
 * tipo de control, descripción y fechas de ciclo de vida (creación, actualización, eliminación).
 * Esta entidad se mapea a la tabla "categorias" en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {
    //id: Identificador único autogenerado.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    //tipoControl: Tipo de control asociado (enum TipoControlCategoria).
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_control", nullable = false, length = 20)
    private TipoControlCategoria tipoControl = TipoControlCategoria.INVENTARIO;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

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
