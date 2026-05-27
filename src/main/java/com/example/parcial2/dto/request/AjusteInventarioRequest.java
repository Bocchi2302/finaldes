package com.example.parcial2.dto.request;

import com.example.parcial2.entity.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AjusteInventarioRequest(
        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        Integer cantidad,

        @NotBlank(message = "El motivo es obligatorio")
        String motivo
) {
}
