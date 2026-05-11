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
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    @Column(name = "registrado_en", nullable = false)
    private OffsetDateTime registradoEn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_usuario_id", nullable = false)
    private User registradoPor;

    @PrePersist
    void prePersist() {
        if (registradoEn == null) {
            registradoEn = OffsetDateTime.now();
        }
        if (fechaVenta == null) {
            fechaVenta = LocalDate.now();
        }
    }
}