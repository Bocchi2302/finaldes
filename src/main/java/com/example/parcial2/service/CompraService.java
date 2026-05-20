package com.papeleria.inteligente.service;

import com.papeleria.inteligente.dto.request.CompraRequest;
import com.papeleria.inteligente.dto.request.DetalleCompraRequest;
import com.papeleria.inteligente.dto.response.CompraResponse;
import com.papeleria.inteligente.entity.*;
import com.papeleria.inteligente.mapper.CompraMapper;
import com.papeleria.inteligente.repository.CompraRepository;
import com.papeleria.inteligente.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CompraService {
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final ProveedorService proveedorService;
    private final CurrentUserService currentUserService;
    private final MovimientoInventarioService movimientoService;
    private final CompraMapper compraMapper;

    public CompraService(CompraRepository compraRepository,
                         ProductoRepository productoRepository,
                         ProductoService productoService,
                         ProveedorService proveedorService,
                         CurrentUserService currentUserService,
                         MovimientoInventarioService movimientoService,
                         CompraMapper compraMapper) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.productoService = productoService;
        this.proveedorService = proveedorService;
        this.currentUserService = currentUserService;
        this.movimientoService = movimientoService;
        this.compraMapper = compraMapper;
    }

    @Transactional
    public CompraResponse registrar(CompraRequest request) {
        User usuario = currentUserService.getAuthenticatedUser();
        Proveedor proveedor = proveedorService.obtenerEntidad(request.proveedorId());
        Compra compra = Compra.builder()
                .fecha(request.fecha())
                .proveedor(proveedor)
                .usuario(usuario)
                .estado(EstadoOperacion.REGISTRADA)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompraRequest item : request.detalles()) {
            Producto producto = productoService.obtenerEntidad(item.productoId());
            BigDecimal subtotal = item.precioUnitario().multiply(BigDecimal.valueOf(item.cantidad()));
            producto.setStock(producto.getStock() + item.cantidad());
            Producto actualizado = productoRepository.save(producto);
            compra.agregarDetalle(DetalleCompra.builder()
                    .producto(actualizado)
                    .cantidad(item.cantidad())
                    .precioUnitario(item.precioUnitario())
                    .subtotal(subtotal)
                    .build());
            total = total.add(subtotal);
        }

        compra.setTotal(total);
        Compra guardada = compraRepository.save(compra);
        guardada.getDetalles().forEach(detalle -> movimientoService.registrar(
                detalle.getProducto(), usuario, TipoMovimiento.ENTRADA, detalle.getCantidad(), "Compra a proveedor", "COMPRA-" + guardada.getId()
        ));
        return compraMapper.toResponse(guardada);
    }

    public List<CompraResponse> listar() {
        return compraRepository.findTop20ByOrderByCreadoEnDesc().stream().map(compraMapper::toResponse).toList();
    }
}
