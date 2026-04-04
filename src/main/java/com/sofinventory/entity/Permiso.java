package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los permisos del sistema.
 * Define las acciones que pueden realizar los usuarios según el módulo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permisos")
public class Permiso {
    /// Identificador único del permiso.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ///Código único del permiso (ej: USER_CREATE, PRODUCT_DELETE).
    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "modulo", nullable = false, length = 40)
    private String modulo;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
