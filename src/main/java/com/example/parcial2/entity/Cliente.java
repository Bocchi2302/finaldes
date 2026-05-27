package com.example.parcial2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(unique = true, length = 40)
    private String documento;

    @Column(length = 40)
    private String telefono;

    @Column(length = 150)
    private String correo;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        OffsetDateTime ahora = OffsetDateTime.now();
        if (creadoEn == null) {
            creadoEn = ahora;
        }
        actualizadoEn = ahora;
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = OffsetDateTime.now();
    }
}
