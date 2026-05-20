package com.papeleria.inteligente.dto.response;

public record ClienteResponse(
        Long id,
        String nombre,
        String documento,
        String telefono,
        String correo,
        Boolean activo
) {
}
