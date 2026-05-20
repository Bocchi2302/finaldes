package com.papeleria.inteligente.dto.response;

import java.math.BigDecimal;

public record ProductoMasVendidoResponse(
        Long productoId,
        String producto,
        Integer unidadesVendidas,
        BigDecimal ingresos
) {
}
