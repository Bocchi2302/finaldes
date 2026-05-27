package com.example.parcial2.dto.response;

import com.example.parcial2.entity.Role;

public record AuthResponse(
        String token,
        Long expiresIn,
        Role rol,
        UserResponse usuario
) {
}
