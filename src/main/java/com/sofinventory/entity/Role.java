package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los roles del sistema.
 * Define los niveles de acceso y agrupación de permisos para los usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    ///Identificador único del rol.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   ///Nombre del rol y descripción
    @Column(name = "nombre", nullable = false, unique = true, length = 60)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
}