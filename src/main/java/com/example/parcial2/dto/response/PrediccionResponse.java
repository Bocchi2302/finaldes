package com.papeleria.inteligente.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PrediccionResponse(
        Long productoId,
        String producto,
        Integer stockActual,
        Integer diasProyectados,
        BigDecimal pendiente,
        BigDecimal intercepto,
        BigDecimal demandaEstimada,
        Integer recomendacionCompra,
        String mensaje,
        List<HistorialPrediccionResponse> historial
) {
}
