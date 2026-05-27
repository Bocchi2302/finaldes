package com.example.parcial2.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductoRequest(
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
        String descripcion,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "El stock mínimo es obligatorio")
        @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
        Integer stockMinimo,

        LocalDate fechaVencimiento,
        Boolean activo,
        Set<Long> proveedorIds
) {
}
