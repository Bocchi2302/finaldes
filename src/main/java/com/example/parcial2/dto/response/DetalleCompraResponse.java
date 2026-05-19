package com.papeleria.inteligente.dto.response;

import java.math.BigDecimal;

public record DetalleCompraResponse(
        Long productoId,
        String producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
