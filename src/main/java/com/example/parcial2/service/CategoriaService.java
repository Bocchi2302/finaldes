package com.papeleria.inteligente.service;

import com.papeleria.inteligente.dto.request.CategoriaRequest;
import com.papeleria.inteligente.dto.response.CategoriaResponse;
import com.papeleria.inteligente.entity.Categoria;
import com.papeleria.inteligente.exception.DuplicateResourceException;
import com.papeleria.inteligente.exception.ResourceNotFoundException;
import com.papeleria.inteligente.mapper.CategoriaMapper;
import com.papeleria.inteligente.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new DuplicateResourceException("Ya existe una categoría con ese nombre");
        }
        return categoriaMapper.toResponse(categoriaRepository.save(categoriaMapper.toEntity(request)));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = obtenerEntidad(id);
        categoriaRepository.findByNombreIgnoreCase(request.nombre()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Ya existe una categoría con ese nombre");
            }
        });
        categoriaMapper.update(categoria, request);
        return categoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void desactivar(Long id) {
        Categoria categoria = obtenerEntidad(id);
        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }

    public CategoriaResponse obtener(Long id) {
        return categoriaMapper.toResponse(obtenerEntidad(id));
    }

    public Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + id));
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAllByOrderByNombreAsc().stream().map(categoriaMapper::toResponse).toList();
    }
}
