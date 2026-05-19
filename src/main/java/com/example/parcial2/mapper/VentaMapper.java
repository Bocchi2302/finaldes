package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.response.DetalleVentaResponse;
import com.papeleria.inteligente.dto.response.VentaResponse;
import com.papeleria.inteligente.entity.Cliente;
import com.papeleria.inteligente.entity.DetalleVenta;
import com.papeleria.inteligente.entity.Venta;
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
