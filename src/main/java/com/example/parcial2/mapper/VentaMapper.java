package com.example.parcial2.mapper;

import com.example.parcial2.dto.response.DetalleVentaResponse;
import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.entity.Cliente;
import com.example.parcial2.entity.DetalleVenta;
import com.example.parcial2.entity.Venta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaMapper {
    public VentaResponse toResponse(Venta venta) {
        Cliente cliente = venta.getCliente();
        List<DetalleVentaResponse> detalles = venta.getDetalles().stream().map(this::toDetalleResponse).toList();
        return new VentaResponse(
                venta.getId(),
                venta.getFecha(),
                venta.getTotal(),
                venta.getEstado(),
                cliente == null ? null : cliente.getId(),
                cliente == null ? "Cliente ocasional" : cliente.getNombre(),
                venta.getUsuario().getId(),
                venta.getUsuario().getNombre(),
                detalles,
                venta.getCreadoEn()
        );
    }

    public DetalleVentaResponse toDetalleResponse(DetalleVenta detalle) {
        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
