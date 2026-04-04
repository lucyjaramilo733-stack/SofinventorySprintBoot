package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
/**
 * Entidad que representa a los proveedores de la ferretería.
 * Almacena información fiscal, de contacto y de ubicación necesaria para
 * la gestión de compras y cumplimiento de requisitos legales (DIAN).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificación única (NIT, CC, RUT) para validaciones fiscales
    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    // Nombre legal registrado en cámara de comercio
    @Column(name = "razon_social", nullable = false, length = 160)
    private String razonSocial;

    // Nombre de marca o "aviso" (opcional, para uso comercial)
    @Column(name = "nombre_comercial", length = 120)
    private String nombreComercial;

    @Column(name = "nombre_contacto", length = 120)
    private String nombreContacto;

    @Column(name = "cargo_contacto", length = 80)
    private String cargoContacto;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "pais", nullable = false, length = 60)
    private String pais = "Colombia";

    @Column(name = "departamento", length = 80)
    private String departamento;

    @Column(name = "ciudad", length = 80)
    private String ciudad;

    /**
     * Define si el proveedor es 'nacional' o 'extranjero'.
     * Útil para cálculos de impuestos y logística de importación.
     */
    @Column(name = "tipo_proveedor", nullable = false, length = 20)
    private String tipoProveedor = "nacional";

    /**
     * Estado operativo: 'activo', 'inactivo'.
     * Controla si el proveedor puede ser seleccionado en nuevas órdenes de compra.
     */
    @Column(name = "estado", nullable = false, length = 10)
    private String estado = "activo";

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // --- SECCIÓN DE AUDITORÍA Y TRAZABILIDAD ---
    // Permite saber quién y cuándo se realizaron cambios en la base de datos de proveedores.

    @Column(name = "fecha_registro", nullable = false)
    private ZonedDateTime fechaRegistro = ZonedDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

    /**
     * Campo para 'Soft Delete' (Borrado Lógico).
     * Si no es nulo, el proveedor se considera eliminado sin perder sus datos históricos.
     */
    @Column(name = "fecha_eliminacion")
    private ZonedDateTime fechaEliminacion;

    // --- RELACIONES ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

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