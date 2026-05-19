package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.request.CategoriaRequest;
import com.papeleria.inteligente.dto.response.CategoriaResponse;
import com.papeleria.inteligente.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRequest request) {
        return Categoria.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .activa(request.activa() == null || request.activa())
                .build();
    }

    public void update(Categoria categoria, CategoriaRequest request) {
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        if (request.activa() != null) {
            categoria.setActiva(request.activa());
        }
    }

    public CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(), categoria.getActiva());
    }
}
