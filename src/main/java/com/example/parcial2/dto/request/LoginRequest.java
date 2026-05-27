package com.example.parcial2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "El correo debe tener un formato válido")
        @NotBlank(message = "El correo es obligatorio")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
