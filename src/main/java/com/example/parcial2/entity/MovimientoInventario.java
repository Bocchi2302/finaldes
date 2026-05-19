package com.papeleria.inteligente.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private OffsetDateTime fecha;

    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(length = 80)
    private String referencia;

    @Column(name = "stock_resultante", nullable = false)
    private Integer stockResultante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @PrePersist
    void prePersist() {
        if (fecha == null) {
            fecha = OffsetDateTime.now();
        }
    }
}
