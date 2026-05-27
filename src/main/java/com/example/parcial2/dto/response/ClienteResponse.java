package com.example.parcial2.dto.response;

public record ClienteResponse(
        Long id,
        String nombre,
        String documento,
        String telefono,
        String correo,
        Boolean activo
) {
}
