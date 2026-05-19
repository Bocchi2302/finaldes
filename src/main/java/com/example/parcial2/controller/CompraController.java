package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.request.CompraRequest;
import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.CompraResponse;
import com.papeleria.inteligente.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ApiResponse<List<CompraResponse>> listar() {
        return ApiResponse.success(200, "Compras consultadas", compraService.listar());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompraResponse>> registrar(@Valid @RequestBody CompraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Compra registrada", compraService.registrar(request)));
    }
}
