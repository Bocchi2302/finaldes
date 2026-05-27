package com.example.parcial2.dto.response;

import com.example.parcial2.entity.Role;

public record UserResponse(
        Long id,
        String nombre,
        String correo,
        Role rol,
        Boolean estado
) {
}
