package com.example.parcial2.mapper;

import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.entity.Venta;
import org.springframework.stereotype.Component;

@Component
public class VentaMapper {

    public VentaResponse toResponse(Venta venta) {
        return VentaResponse.builder()
                .id(venta.getId())
                .productoId(venta.getProducto().getId())
                .productoNombre(venta.getProducto().getNombre())
                .cantidad(venta.getCantidad())
                .precioUnitario(venta.getPrecioUnitario())
                .total(venta.getTotal())
                .fechaVenta(venta.getFechaVenta())
                .registradoEn(venta.getRegistradoEn())
                .registradoPorId(venta.getRegistradoPor().getId())
                .registradoPorNombre(venta.getRegistradoPor().getFullName())
                .build();
    }
}