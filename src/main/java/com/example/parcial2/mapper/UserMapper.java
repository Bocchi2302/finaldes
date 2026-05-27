package com.example.parcial2.mapper;

import com.example.parcial2.dto.response.UserResponse;
import com.example.parcial2.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNombre(),
                user.getCorreo(),
                user.getRol(),
                user.getEstado()
        );
    }
}
