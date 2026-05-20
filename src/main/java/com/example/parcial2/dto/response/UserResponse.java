package com.papeleria.inteligente.dto.response;

import com.papeleria.inteligente.entity.Role;

public record UserResponse(
        Long id,
        String nombre,
        String correo,
        Role rol,
        Boolean estado
) {
}
