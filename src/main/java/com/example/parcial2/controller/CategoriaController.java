package com.example.parcial2.controller;

import com.example.parcial2.dto.request.CategoriaRequest;
import com.example.parcial2.dto.response.ApiResponse;
import com.example.parcial2.dto.response.CategoriaResponse;
import com.example.parcial2.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ApiResponse<List<CategoriaResponse>> listar() {
        return ApiResponse.success(200, "Categorías consultadas", categoriaService.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoriaResponse> obtener(@PathVariable Long id) {
        return ApiResponse.success(200, "Categoría consultada", categoriaService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponse>> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Categoría creada", categoriaService.crear(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoriaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return ApiResponse.success(200, "Categoría actualizada", categoriaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        categoriaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
