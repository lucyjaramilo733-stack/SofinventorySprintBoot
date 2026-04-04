package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entidad que relaciona roles con permisos.
 * Permite asignar permisos específicos a cada rol del sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol_permisos")
public class RolPermiso {
    ///Identificador único de la relación rol-permiso.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ///Relaciones con role y Permiso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Role rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permiso permiso;
    //Indica si la relación está activa.
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}