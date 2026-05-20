package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.request.ProductoRequest;
import com.papeleria.inteligente.dto.response.ProductoResponse;
import com.papeleria.inteligente.entity.Categoria;
import com.papeleria.inteligente.entity.Producto;
import com.papeleria.inteligente.entity.Proveedor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class ProductoMapper {
    public Producto toEntity(ProductoRequest request, Categoria categoria, Collection<Proveedor> proveedores) {
        return Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .categoria(categoria)
                .precio(request.precio())
                .stock(request.stock())
                .stockMinimo(request.stockMinimo())
                .fechaVencimiento(request.fechaVencimiento())
                .activo(request.activo() == null || request.activo())
                .proveedores(new LinkedHashSet<>(proveedores))
                .build();
    }

    public void update(Producto producto, ProductoRequest request, Categoria categoria, Collection<Proveedor> proveedores) {
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setCategoria(categoria);
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setStockMinimo(request.stockMinimo());
        producto.setFechaVencimiento(request.fechaVencimiento());
        if (request.activo() != null) {
            producto.setActivo(request.activo());
        }
        producto.getProveedores().clear();
        producto.getProveedores().addAll(proveedores);
    }

    public ProductoResponse toResponse(Producto producto) {
        List<String> proveedores = producto.getProveedores() == null
                ? List.of()
                : producto.getProveedores().stream().map(Proveedor::getNombre).sorted().toList();
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo(),
                producto.getActivo(),
                producto.getFechaVencimiento(),
                calcularEstadoStock(producto),
                proveedores,
                producto.getCreadoEn(),
                producto.getActualizadoEn()
        );
    }

    private String calcularEstadoStock(Producto producto) {
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            return "INACTIVO";
        }
        if (producto.getStock() == 0) {
            return "AGOTADO";
        }
        if (producto.getStock() <= producto.getStockMinimo()) {
            return "BAJO_STOCK";
        }
        return "DISPONIBLE";
    }
}
