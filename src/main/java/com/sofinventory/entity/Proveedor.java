package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "razon_social", nullable = false, length = 160)
    private String razonSocial;

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

    @Column(name = "tipo_proveedor", nullable = false, length = 20)
    private String tipoProveedor = "nacional";

    @Column(name = "estado", nullable = false, length = 10)
    private String estado = "activo";

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    private ZonedDateTime fechaRegistro = ZonedDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private ZonedDateTime fechaActualizacion = ZonedDateTime.now();

    @Column(name = "fecha_eliminacion")
    private ZonedDateTime fechaEliminacion;

    // Relaciones
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