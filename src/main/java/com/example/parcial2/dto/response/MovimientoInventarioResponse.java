package com.papeleria.inteligente.dto.response;

import com.papeleria.inteligente.entity.TipoMovimiento;

import java.time.OffsetDateTime;

public record MovimientoInventarioResponse(
        Long id,
        TipoMovimiento tipoMovimiento,
        Integer cantidad,
        OffsetDateTime fecha,
        String motivo,
        String referencia,
        Integer stockResultante,
        Long productoId,
        String producto,
        Long usuarioId,
        String usuario
) {
}
