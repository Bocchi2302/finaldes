package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.request.ProveedorRequest;
import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.ProveedorResponse;
import com.papeleria.inteligente.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {
    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ApiResponse<List<ProveedorResponse>> listar() {
        return ApiResponse.success(200, "Proveedores consultados", proveedorService.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProveedorResponse> obtener(@PathVariable Long id) {
        return ApiResponse.success(200, "Proveedor consultado", proveedorService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProveedorResponse>> crear(@Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Proveedor creado", proveedorService.crear(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProveedorResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        return ApiResponse.success(200, "Proveedor actualizado", proveedorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        proveedorService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
