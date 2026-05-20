package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.request.AjusteInventarioRequest;
import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.MovimientoInventarioResponse;
import com.papeleria.inteligente.dto.response.ProductoResponse;
import com.papeleria.inteligente.service.MovimientoInventarioService;
import com.papeleria.inteligente.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {
    private final ProductoService productoService;
    private final MovimientoInventarioService movimientoService;

    public InventarioController(ProductoService productoService, MovimientoInventarioService movimientoService) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public ApiResponse<List<ProductoResponse>> listarInventario() {
        return ApiResponse.success(200, "Inventario consultado", productoService.listarActivos());
    }

    @GetMapping("/bajo-stock")
    public ApiResponse<List<ProductoResponse>> bajoStock() {
        return ApiResponse.success(200, "Productos con bajo stock", productoService.listarBajoStock());
    }

    @GetMapping("/movimientos")
    public ApiResponse<List<MovimientoInventarioResponse>> movimientos() {
        return ApiResponse.success(200, "Movimientos consultados", movimientoService.listar());
    }

    @PostMapping("/ajustes")
    public ResponseEntity<ApiResponse<MovimientoInventarioResponse>> ajustar(@Valid @RequestBody AjusteInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Ajuste registrado", movimientoService.ajustar(request)));
    }
}
