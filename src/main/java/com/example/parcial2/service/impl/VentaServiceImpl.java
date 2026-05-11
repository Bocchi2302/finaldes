package com.example.parcial2.service.impl;

import com.example.parcial2.dto.request.VentaRequest;
import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.entity.Producto;
import com.example.parcial2.entity.User;
import com.example.parcial2.entity.Venta;
import com.example.parcial2.exception.custom.InsufficientStockException;
import com.example.parcial2.exception.custom.ResourceNotFoundException;
import com.example.parcial2.mapper.VentaMapper;
import com.example.parcial2.repository.ProductoRepository;
import com.example.parcial2.repository.UserRepository;
import com.example.parcial2.repository.VentaRepository;
import com.example.parcial2.service.VentaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;
    private final VentaMapper ventaMapper;

    public VentaServiceImpl(VentaRepository ventaRepository,
                            ProductoRepository productoRepository,
                            UserRepository userRepository,
                            VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.userRepository = userRepository;
        this.ventaMapper = ventaMapper;
    }

    @Override
    @Transactional
    public VentaResponse registrarVenta(VentaRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto with id " + request.getProductoId() + " not found"));

        if (!producto.isActivo()) {
            throw new ResourceNotFoundException("Producto with id " + request.getProductoId() + " not found");
        }

        if (producto.getStock() < request.getCantidad()) {
            throw new InsufficientStockException("Stock insuficiente para el producto " + producto.getNombre());
        }

        User registradoPor = getAuthenticatedUser();
        BigDecimal total = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad()));

        producto.setStock(producto.getStock() - request.getCantidad());
        productoRepository.save(producto);

        Venta venta = Venta.builder()
                .producto(producto)
                .cantidad(request.getCantidad())
                .precioUnitario(producto.getPrecioUnitario())
                .total(total)
                .fechaVenta(request.getFechaVenta())
                .registradoPor(registradoPor)
                .build();

        return ventaMapper.toResponse(ventaRepository.save(venta));
    }

    @Override
    public List<VentaResponse> listarVentas() {
        return ventaRepository.findTop20ByOrderByFechaVentaDesc().stream()
                .map(ventaMapper::toResponse)
                .toList();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("No se pudo identificar el usuario autenticado");
        }
        return user;
    }
}