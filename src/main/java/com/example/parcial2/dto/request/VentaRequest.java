package com.example.parcial2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public record VentaRequest(
        Long clienteId,
        LocalDate fecha,

        @Valid
        @NotEmpty(message = "La venta debe tener al menos un producto")
        List<DetalleVentaRequest> detalles
) {
}
