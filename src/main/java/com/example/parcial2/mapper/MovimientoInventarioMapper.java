package com.papeleria.inteligente.mapper;

import com.papeleria.inteligente.dto.response.MovimientoInventarioResponse;
import com.papeleria.inteligente.entity.MovimientoInventario;
import org.springframework.stereotype.Component;

@Component
public class MovimientoInventarioMapper {
    public MovimientoInventarioResponse toResponse(MovimientoInventario movimiento) {
        return new MovimientoInventarioResponse(
                movimiento.getId(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getMotivo(),
                movimiento.getReferencia(),
                movimiento.getStockResultante(),
                movimiento.getProducto().getId(),
                movimiento.getProducto().getNombre(),
                movimiento.getUsuario().getId(),
                movimiento.getUsuario().getNombre()
        );
    }
}
