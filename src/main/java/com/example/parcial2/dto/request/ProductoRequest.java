package com.example.parcial2.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProductoRequest {

    @NotBlank(message = "must not be blank")
    @Size(max = 150, message = "must be at most 150 characters")
    private String nombre;

    @NotBlank(message = "must not be blank")
    @Size(max = 80, message = "must be at most 80 characters")
    private String categoria;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0.01", message = "must be greater than 0")
    private BigDecimal precioUnitario;

    @NotNull(message = "must not be null")
    @PositiveOrZero(message = "must be zero or greater")
    private Integer stock;

    @NotNull(message = "must not be null")
    @PositiveOrZero(message = "must be zero or greater")
    private Integer stockMinimo;

    private Boolean activo;

    private LocalDate fechaVencimiento;
}