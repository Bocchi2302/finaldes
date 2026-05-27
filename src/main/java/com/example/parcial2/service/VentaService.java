package com.example.parcial2.service;

import com.example.parcial2.dto.request.DetalleVentaRequest;
import com.example.parcial2.dto.request.VentaRequest;
import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.entity.*;
import com.example.parcial2.exception.InsufficientStockException;
import com.example.parcial2.mapper.VentaMapper;
import com.example.parcial2.repository.ProductoRepository;
import com.example.parcial2.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final CurrentUserService currentUserService;
    private final MovimientoInventarioService movimientoService;
    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository,
                        ProductoRepository productoRepository,
                        ProductoService productoService,
                        ClienteService clienteService,
                        CurrentUserService currentUserService,
                        MovimientoInventarioService movimientoService,
                        VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.currentUserService = currentUserService;
        this.movimientoService = movimientoService;
        this.ventaMapper = ventaMapper;
    }

    @Transactional
    public VentaResponse registrar(VentaRequest request) {
        User usuario = currentUserService.getAuthenticatedUser();
        Cliente cliente = request.clienteId() == null ? null : clienteService.obtenerEntidad(request.clienteId());
        Venta venta = Venta.builder()
                .fecha(request.fecha() == null ? LocalDate.now() : request.fecha())
                .cliente(cliente)
                .usuario(usuario)
                .estado(EstadoOperacion.REGISTRADA)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaRequest item : request.detalles()) {
            Producto producto = productoService.obtenerEntidad(item.productoId());
            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new InsufficientStockException("El producto " + producto.getNombre() + " está inactivo");
            }
            if (producto.getStock() < item.cantidad()) {
                throw new InsufficientStockException("Stock insuficiente para " + producto.getNombre());
            }
            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad()));
            producto.setStock(producto.getStock() - item.cantidad());
            Producto actualizado = productoRepository.save(producto);
            venta.agregarDetalle(DetalleVenta.builder()
                    .producto(actualizado)
                    .cantidad(item.cantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build());
            total = total.add(subtotal);
        }

        venta.setTotal(total);
        Venta guardada = ventaRepository.save(venta);
        guardada.getDetalles().forEach(detalle -> movimientoService.registrar(
                detalle.getProducto(), usuario, TipoMovimiento.SALIDA, detalle.getCantidad(), "Venta registrada", "VENTA-" + guardada.getId()
        ));
        return ventaMapper.toResponse(guardada);
    }

    public List<VentaResponse> listar() {
        return ventaRepository.findTop20ByOrderByCreadoEnDesc().stream().map(ventaMapper::toResponse).toList();
    }
}
