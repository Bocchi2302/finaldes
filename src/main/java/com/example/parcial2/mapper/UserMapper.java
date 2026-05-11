package com.example.parcial2.mapper;

import com.example.parcial2.dto.request.RegisterRequest;
import com.example.parcial2.dto.response.UserResponse;
import com.example.parcial2.entity.Role;
import com.example.parcial2.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(Role.ROLE_EMPLEADO)
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
