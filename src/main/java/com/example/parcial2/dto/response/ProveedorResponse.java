package com.example.parcial2.dto.response;

public record ProveedorResponse(
        Long id,
        String nombre,
        String telefono,
        String correo,
        String direccion,
        Boolean activo
) {
}
