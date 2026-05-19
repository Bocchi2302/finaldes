package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.request.VentaRequest;
import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.VentaResponse;
import com.papeleria.inteligente.service.VentaService;
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
