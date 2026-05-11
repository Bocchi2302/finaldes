package com.example.parcial2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

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
    }

    @PreUpdate
    void preUpdate() {
        actualizadoEn = OffsetDateTime.now();
    }
}