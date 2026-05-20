package com.papeleria.inteligente.dto.response;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        Boolean activa
) {
}
