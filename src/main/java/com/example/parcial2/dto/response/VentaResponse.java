package com.example.parcial2.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Builder
public class VentaResponse {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private LocalDate fechaVenta;
    private OffsetDateTime registradoEn;
    private Long registradoPorId;
    private String registradoPorNombre;
}