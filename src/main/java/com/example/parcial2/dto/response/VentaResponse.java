package com.papeleria.inteligente.dto.response;

import com.papeleria.inteligente.entity.EstadoOperacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record VentaResponse(
        Long id,
        LocalDate fecha,
        BigDecimal total,
        EstadoOperacion estado,
        Long clienteId,
        String cliente,
        Long usuarioId,
        String usuario,
        List<DetalleVentaResponse> detalles,
        OffsetDateTime creadoEn
) {
}
