package com.papeleria.inteligente.dto.request;

import com.papeleria.inteligente.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Email(message = "El correo debe tener un formato válido")
        @NotBlank(message = "El correo es obligatorio")
        String correo,

        @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        Role rol
) {
}
