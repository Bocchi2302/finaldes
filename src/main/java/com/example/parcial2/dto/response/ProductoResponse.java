package com.example.parcial2.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        Long categoriaId,
        String categoria,
        BigDecimal precio,
        Integer stock,
        Integer stockMinimo,
        Boolean activo,
        LocalDate fechaVencimiento,
        String estadoStock,
        List<String> proveedores,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn
) {
}
