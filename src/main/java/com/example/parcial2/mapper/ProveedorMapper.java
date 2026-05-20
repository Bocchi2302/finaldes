package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.request.ProveedorRequest;
import com.papeleria.inteligente.dto.response.ProveedorResponse;
import com.papeleria.inteligente.entity.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {
    public Proveedor toEntity(ProveedorRequest request) {
        return Proveedor.builder()
                .nombre(request.nombre())
                .telefono(request.telefono())
                .correo(request.correo())
                .direccion(request.direccion())
                .activo(request.activo() == null || request.activo())
                .build();
    }

    public void update(Proveedor proveedor, ProveedorRequest request) {
        proveedor.setNombre(request.nombre());
        proveedor.setTelefono(request.telefono());
        proveedor.setCorreo(request.correo());
        proveedor.setDireccion(request.direccion());
        if (request.activo() != null) {
            proveedor.setActivo(request.activo());
        }
    }

    public ProveedorResponse toResponse(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getCorreo(),
                proveedor.getDireccion(),
                proveedor.getActivo()
        );
    }
}
