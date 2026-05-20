package com.papeleria.inteligente.dto.response;

import java.math.BigDecimal;

public record DetalleVentaResponse(
        Long productoId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
