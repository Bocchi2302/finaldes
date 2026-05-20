package com.papeleria.inteligente.service;

import com.papeleria.inteligente.dto.request.ProductoRequest;
import com.papeleria.inteligente.dto.response.ProductoResponse;
import com.papeleria.inteligente.entity.Categoria;
import com.papeleria.inteligente.entity.Producto;
import com.papeleria.inteligente.entity.Proveedor;
import com.papeleria.inteligente.exception.DuplicateResourceException;
import com.papeleria.inteligente.exception.ResourceNotFoundException;
import com.papeleria.inteligente.mapper.ProductoMapper;
import com.papeleria.inteligente.repository.ProductoRepository;
import com.papeleria.inteligente.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaService categoriaService,
                           ProveedorRepository proveedorRepository,
                           ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.proveedorRepository = proveedorRepository;
        this.productoMapper = productoMapper;
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        validarNombreDuplicado(request.nombre(), null);
        Categoria categoria = categoriaService.obtenerEntidad(request.categoriaId());
        Set<Proveedor> proveedores = buscarProveedores(request.proveedorIds());
        Producto producto = productoMapper.toEntity(request, categoria, proveedores);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = obtenerEntidad(id);
        validarNombreDuplicado(request.nombre(), id);
        Categoria categoria = categoriaService.obtenerEntidad(request.categoriaId());
        Set<Proveedor> proveedores = buscarProveedores(request.proveedorIds());
        productoMapper.update(producto, request, categoria, proveedores);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = obtenerEntidad(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public ProductoResponse obtener(Long id) {
        return productoMapper.toResponse(obtenerEntidad(id));
    }

    public Producto obtenerEntidad(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    public List<ProductoResponse> listar() {
        return productoRepository.findAllByOrderByNombreAsc().stream().map(productoMapper::toResponse).toList();
    }

    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc().stream().map(productoMapper::toResponse).toList();
    }

    public List<ProductoResponse> listarBajoStock() {
        return productoRepository.findProductosBajoStock().stream().map(productoMapper::toResponse).toList();
    }

    private void validarNombreDuplicado(String nombre, Long excludeId) {
        productoRepository.findByNombreIgnoreCase(nombre).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new DuplicateResourceException("Ya existe un producto con el nombre " + nombre);
            }
        });
    }

    private Set<Proveedor> buscarProveedores(Set<Long> proveedorIds) {
        if (proveedorIds == null || proveedorIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        Set<Proveedor> proveedores = new LinkedHashSet<>(proveedorRepository.findAllById(proveedorIds));
        if (proveedores.size() != proveedorIds.size()) {
            throw new ResourceNotFoundException("Uno o más proveedores no existen");
        }
        return proveedores;
    }
}
