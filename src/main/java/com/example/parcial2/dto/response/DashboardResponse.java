package com.papeleria.inteligente.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        long productosActivos,
        int productosBajoStock,
        BigDecimal ventasDelDia,
        BigDecimal ingresosDelMes,
        List<ProductoResponse> bajoStock,
        List<ProductoMasVendidoResponse> productosMasVendidos
) {
}
