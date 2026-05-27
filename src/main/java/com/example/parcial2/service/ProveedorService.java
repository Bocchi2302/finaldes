package com.example.parcial2.service;

import com.example.parcial2.dto.request.ProveedorRequest;
import com.example.parcial2.dto.response.ProveedorResponse;
import com.example.parcial2.entity.Proveedor;
import com.example.parcial2.exception.DuplicateResourceException;
import com.example.parcial2.exception.ResourceNotFoundException;
import com.example.parcial2.mapper.ProveedorMapper;
import com.example.parcial2.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Transactional
    public ProveedorResponse crear(ProveedorRequest request) {
        proveedorRepository.findByNombreIgnoreCase(request.nombre()).ifPresent(p -> {
            throw new DuplicateResourceException("Ya existe un proveedor con ese nombre");
        });
        return proveedorMapper.toResponse(proveedorRepository.save(proveedorMapper.toEntity(request)));
    }

    @Transactional
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = obtenerEntidad(id);
        proveedorRepository.findByNombreIgnoreCase(request.nombre()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Ya existe un proveedor con ese nombre");
            }
        });
        proveedorMapper.update(proveedor, request);
        return proveedorMapper.toResponse(proveedorRepository.save(proveedor));
    }

    @Transactional
    public void desactivar(Long id) {
        Proveedor proveedor = obtenerEntidad(id);
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    public ProveedorResponse obtener(Long id) {
        return proveedorMapper.toResponse(obtenerEntidad(id));
    }

    public Proveedor obtenerEntidad(Long id) {
        return proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + id));
    }

    public List<ProveedorResponse> listar() {
        return proveedorRepository.findAllByOrderByNombreAsc().stream().map(proveedorMapper::toResponse).toList();
    }
}
