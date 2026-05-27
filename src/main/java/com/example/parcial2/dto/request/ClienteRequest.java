package com.example.parcial2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El nombre del cliente es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Size(max = 40, message = "El documento no puede superar 40 caracteres")
        String documento,

        @Size(max = 40, message = "El teléfono no puede superar 40 caracteres")
        String telefono,

        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 150, message = "El correo no puede superar 150 caracteres")
        String correo,

        Boolean activo
) {
}
