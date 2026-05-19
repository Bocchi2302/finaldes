package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.request.ClienteRequest;
import com.papeleria.inteligente.dto.response.ClienteResponse;
import com.papeleria.inteligente.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toEntity(ClienteRequest request) {
        return Cliente.builder()
                .nombre(request.nombre())
                .documento(request.documento())
                .telefono(request.telefono())
                .correo(request.correo())
                .activo(request.activo() == null || request.activo())
                .build();
    }

    public void update(Cliente cliente, ClienteRequest request) {
        cliente.setNombre(request.nombre());
        cliente.setDocumento(request.documento());
        cliente.setTelefono(request.telefono());
        cliente.setCorreo(request.correo());
        if (request.activo() != null) {
            cliente.setActivo(request.activo());
        }
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(), cliente.getNombre(), cliente.getDocumento(), cliente.getTelefono(), cliente.getCorreo(), cliente.getActivo()
        );
    }
}
