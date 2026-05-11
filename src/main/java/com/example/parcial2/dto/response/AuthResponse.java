package com.example.parcial2.dto.response;

import com.example.parcial2.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private Long expiresIn;
    private Role role;
    private UserResponse user;
}
