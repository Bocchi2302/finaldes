package com.papeleria.inteligente.dto.response;

import java.time.LocalDate;

public record HistorialPrediccionResponse(
        LocalDate fecha,
        Integer cantidadVendida
) {
}
