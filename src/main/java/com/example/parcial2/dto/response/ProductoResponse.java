package com.example.parcial2.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Builder
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String categoria;
    private BigDecimal precioUnitario;
    private Integer stock;
    private Integer stockMinimo;
    private boolean activo;
    private LocalDate fechaVencimiento;
    private String estadoStock;
    private OffsetDateTime creadoEn;
    private OffsetDateTime actualizadoEn;
}