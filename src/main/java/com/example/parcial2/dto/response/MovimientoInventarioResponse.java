package com.example.parcial2.dto.response;

import com.example.parcial2.entity.TipoMovimiento;

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
