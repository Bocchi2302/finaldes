package com.example.parcial2.dto.response;

import java.time.LocalDate;

public record HistorialPrediccionResponse(
        LocalDate fecha,
        Integer cantidadVendida
) {
}
