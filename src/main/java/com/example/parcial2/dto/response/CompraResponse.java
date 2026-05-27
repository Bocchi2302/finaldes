package com.example.parcial2.dto.response;

import com.example.parcial2.entity.EstadoOperacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record CompraResponse(
        Long id,
        LocalDate fecha,
        BigDecimal total,
        EstadoOperacion estado,
        Long proveedorId,
        String proveedor,
        Long usuarioId,
        String usuario,
        List<DetalleCompraResponse> detalles,
        OffsetDateTime creadoEn
) {
}
