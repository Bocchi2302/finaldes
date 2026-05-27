package com.example.parcial2.service;

import com.example.parcial2.dto.request.AjusteInventarioRequest;
import com.example.parcial2.dto.response.MovimientoInventarioResponse;
import com.example.parcial2.entity.MovimientoInventario;
import com.example.parcial2.entity.Producto;
import com.example.parcial2.entity.TipoMovimiento;
import com.example.parcial2.entity.User;
import com.example.parcial2.exception.BusinessRuleException;
import com.example.parcial2.mapper.MovimientoInventarioMapper;
import com.example.parcial2.repository.MovimientoInventarioRepository;
import com.example.parcial2.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MovimientoInventarioService {
    private final MovimientoInventarioRepository movimientoRepository;
    private final MovimientoInventarioMapper movimientoMapper;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final CurrentUserService currentUserService;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoRepository,
                                       MovimientoInventarioMapper movimientoMapper,
                                       ProductoService productoService,
                                       ProductoRepository productoRepository,
                                       CurrentUserService currentUserService) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
        this.productoService = productoService;
        this.productoRepository = productoRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public MovimientoInventario registrar(Producto producto, User usuario, TipoMovimiento tipo, Integer cantidad, String motivo, String referencia) {
        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .usuario(usuario)
                .tipoMovimiento(tipo)
                .cantidad(cantidad)
                .motivo(motivo)
                .referencia(referencia)
                .stockResultante(producto.getStock())
                .build();
        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public MovimientoInventarioResponse ajustar(AjusteInventarioRequest request) {
        Producto producto = productoService.obtenerEntidad(request.productoId());
        User usuario = currentUserService.getAuthenticatedUser();
        int stockNuevo = calcularStock(producto.getStock(), request.tipoMovimiento(), request.cantidad());
        producto.setStock(stockNuevo);
        Producto actualizado = productoRepository.save(producto);
        MovimientoInventario movimiento = registrar(actualizado, usuario, request.tipoMovimiento(), request.cantidad(), request.motivo(), "AJUSTE_MANUAL");
        return movimientoMapper.toResponse(movimiento);
    }

    public List<MovimientoInventarioResponse> listar() {
        return movimientoRepository.findTop50ByOrderByFechaDesc().stream().map(movimientoMapper::toResponse).toList();
    }

    private int calcularStock(int stockActual, TipoMovimiento tipo, int cantidad) {
        return switch (tipo) {
            case ENTRADA -> stockActual + cantidad;
            case SALIDA -> {
                if (stockActual < cantidad) {
                    throw new BusinessRuleException("No hay stock suficiente para realizar la salida de inventario");
                }
                yield stockActual - cantidad;
            }
            case AJUSTE -> cantidad;
        };
    }
}
