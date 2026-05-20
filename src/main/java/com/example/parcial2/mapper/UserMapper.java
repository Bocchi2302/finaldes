package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.response.UserResponse;
import com.papeleria.inteligente.entity.User;
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
