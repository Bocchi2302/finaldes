package com.example.parcial2.controller;

import com.example.parcial2.dto.request.VentaRequest;
import com.example.parcial2.dto.response.ApiResponse;
import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ApiResponse<List<VentaResponse>> listar() {
        return ApiResponse.success(200, "Ventas consultadas", ventaService.listar());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VentaResponse>> registrar(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Venta registrada", ventaService.registrar(request)));
    }
}
