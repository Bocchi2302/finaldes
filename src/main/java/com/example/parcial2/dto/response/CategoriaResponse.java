package com.example.parcial2.dto.response;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        Boolean activa
) {
}
