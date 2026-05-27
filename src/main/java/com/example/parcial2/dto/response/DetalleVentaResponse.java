package com.example.parcial2.dto.response;

import java.math.BigDecimal;

public record DetalleVentaResponse(
        Long productoId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
