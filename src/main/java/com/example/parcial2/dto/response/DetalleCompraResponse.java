package com.example.parcial2.dto.response;

import java.math.BigDecimal;

public record DetalleCompraResponse(
        Long productoId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
