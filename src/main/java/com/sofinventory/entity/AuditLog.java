package com.sofinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

/**
 * Entidad que registra las acciones realizadas por los usuarios en el sistema.
 * Permite llevar control de auditoría y trazabilidad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_log")
public class AuditLog {

     ///Identificador único del registro de auditoría.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario que ejecutó la acción.
     * Relación muchos a uno (un usuario puede generar múltiples registros).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

     ///Información básica de la acción realizada.

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha = ZonedDateTime.now();
}