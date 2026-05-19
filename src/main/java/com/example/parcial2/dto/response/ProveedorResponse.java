package com.papeleria.inteligente.dto.response;

public record ProveedorResponse(
        Long id,
        String nombre,
        String telefono,
        String correo,
        String direccion,
        Boolean activo
) {
}
