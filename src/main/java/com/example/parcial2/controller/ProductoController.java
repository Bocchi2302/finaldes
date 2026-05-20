package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.request.ProductoRequest;
import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.ProductoResponse;
import com.papeleria.inteligente.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ApiResponse<List<ProductoResponse>> listar() {
        return ApiResponse.success(200, "Productos consultados", productoService.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductoResponse> obtener(@PathVariable Long id) {
        return ApiResponse.success(200, "Producto consultado", productoService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponse>> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Producto creado", productoService.crear(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return ApiResponse.success(200, "Producto actualizado", productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
