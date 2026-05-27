package com.example.parcial2.mapper;

import com.example.parcial2.dto.response.CompraResponse;
import com.example.parcial2.dto.response.DetalleCompraResponse;
import com.example.parcial2.entity.Compra;
import com.example.parcial2.entity.DetalleCompra;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompraMapper {
    public CompraResponse toResponse(Compra compra) {
        List<DetalleCompraResponse> detalles = compra.getDetalles().stream().map(this::toDetalleResponse).toList();
        return new CompraResponse(
                compra.getId(),
                compra.getFecha(),
                compra.getTotal(),
                compra.getEstado(),
                compra.getProveedor().getId(),
                compra.getProveedor().getNombre(),
                compra.getUsuario().getId(),
                compra.getUsuario().getNombre(),
                detalles,
                compra.getCreadoEn()
        );
    }

    public DetalleCompraResponse toDetalleResponse(DetalleCompra detalle) {
        return new DetalleCompraResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
