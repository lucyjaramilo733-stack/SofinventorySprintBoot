package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los tipos de documento de identificación.
 * Se utiliza para clasificar documentos como cédula, NIT, pasaporte, etc.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_documento")
public class TipoDocumento {
    ///Identificador único del tipo de documento.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ///Código único del tipo de documento (ej: CC, NIT).
    @Column(name = "codigo", nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;
}
