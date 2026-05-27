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
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activa;

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
        if (activa == null) {
            activa = true;
        }
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = OffsetDateTime.now();
    }
}
