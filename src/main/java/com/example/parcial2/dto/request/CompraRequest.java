package com.example.parcial2.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CompraRequest(
        @NotNull(message = "El proveedor es obligatorio")
        Long proveedorId,

        LocalDate fecha,

        @Valid
        @NotEmpty(message = "La compra debe tener al menos un producto")
        List<DetalleCompraRequest> detalles
) {
}
