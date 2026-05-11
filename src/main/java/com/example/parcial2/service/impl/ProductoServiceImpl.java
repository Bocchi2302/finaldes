package com.example.parcial2.service.impl;

import com.example.parcial2.dto.request.ProductoRequest;
import com.example.parcial2.dto.response.ProductoResponse;
import com.example.parcial2.entity.Producto;
import com.example.parcial2.entity.User;
import com.example.parcial2.exception.custom.DuplicateResourceException;
import com.example.parcial2.exception.custom.ResourceNotFoundException;
import com.example.parcial2.exception.custom.UnauthorizedAccessException;
import com.example.parcial2.mapper.ProductoMapper;
import com.example.parcial2.repository.ProductoRepository;
import com.example.parcial2.repository.UserRepository;
import com.example.parcial2.service.ProductoCommandService;
import com.example.parcial2.service.ProductoPermissionService;
import com.example.parcial2.service.ProductoQueryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoCommandService, ProductoQueryService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final UserRepository userRepository;
    private final ProductoPermissionService productoPermissionService;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               ProductoMapper productoMapper,
                               UserRepository userRepository,
                               ProductoPermissionService productoPermissionService) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.userRepository = userRepository;
        this.productoPermissionService = productoPermissionService;
    }

    @Override
    @Transactional
    public ProductoResponse crearProducto(ProductoRequest request) {
        validateDuplicateNombre(request.getNombre(), null);
        Producto producto = productoMapper.toEntity(request);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizarProducto(Long id, ProductoRequest request) {
        Producto producto = findProducto(id);
        validateDuplicateNombre(request.getNombre(), id);

        productoMapper.updateEntity(producto, request);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = findProducto(id);
        User user = getAuthenticatedUser();
        productoPermissionService.validarPermisoEliminarProducto(user);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public ProductoResponse obtenerProductoPorId(Long id) {
        return productoMapper.toResponse(findProducto(id));
    }

    @Override
    public List<ProductoResponse> listarProductos() {
        return productoRepository.findAll().stream().map(productoMapper::toResponse).toList();
    }

    private Producto findProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto with id " + id + " not found"));
    }

    private void validateDuplicateNombre(String nombre, Long excludeId) {
        productoRepository.findByNombreIgnoreCase(nombre).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new DuplicateResourceException("Producto with name " + nombre + " already exists");
            }
        });
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new UnauthorizedAccessException("No se pudo identificar el usuario autenticado");
        }
        return user;
    }
}