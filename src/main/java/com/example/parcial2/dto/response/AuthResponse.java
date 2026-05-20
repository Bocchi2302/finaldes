package com.papeleria.inteligente.dto.response;

import com.papeleria.inteligente.entity.Role;

public record AuthResponse(
        String token,
        Long expiresIn,
        Role rol,
        UserResponse usuario
) {
}
