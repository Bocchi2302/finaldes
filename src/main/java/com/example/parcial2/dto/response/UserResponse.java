package com.example.parcial2.dto.response;

import com.example.parcial2.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
