package com.example.parcial2.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VentaRequest {

    @NotNull(message = "must not be null")
    private Long productoId;

    @NotNull(message = "must not be null")
    @Positive(message = "must be greater than 0")
    private Integer cantidad;

    private LocalDate fechaVenta;
}