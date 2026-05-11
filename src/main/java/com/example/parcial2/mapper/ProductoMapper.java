package com.example.parcial2.mapper;

import com.example.parcial2.dto.request.ProductoRequest;
import com.example.parcial2.dto.response.ProductoResponse;
import com.example.parcial2.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        return Producto.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .precioUnitario(request.getPrecioUnitario())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .activo(request.getActivo() == null || request.getActivo())
                .fechaVencimiento(request.getFechaVencimiento())
                .build();
    }

    public void updateEntity(Producto producto, ProductoRequest request) {
        producto.setNombre(request.getNombre());
        producto.setCategoria(request.getCategoria());
        producto.setPrecioUnitario(request.getPrecioUnitario());
        producto.setStock(request.getStock());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setActivo(request.getActivo() == null || request.getActivo());
        producto.setFechaVencimiento(request.getFechaVencimiento());
    }

    public ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .precioUnitario(producto.getPrecioUnitario())
                .stock(producto.getStock())
                .stockMinimo(producto.getStockMinimo())
                .activo(producto.isActivo())
                .fechaVencimiento(producto.getFechaVencimiento())
                .estadoStock(resolveEstadoStock(producto))
                .creadoEn(producto.getCreadoEn())
                .actualizadoEn(producto.getActualizadoEn())
                .build();
    }

    private String resolveEstadoStock(Producto producto) {
        if (!producto.isActivo()) {
            return "INACTIVO";
        }
        if (producto.getStock() == null || producto.getStock() <= 0) {
            return "AGOTADO";
        }
        if (producto.getStock() <= producto.getStockMinimo()) {
            return "BAJO_STOCK";
        }
        return "DISPONIBLE";
    }
}