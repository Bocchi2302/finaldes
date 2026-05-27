package com.example.parcial2.controller;

import com.example.parcial2.dto.request.ClienteRequest;
import com.example.parcial2.dto.response.ApiResponse;
import com.example.parcial2.dto.response.ClienteResponse;
import com.example.parcial2.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ApiResponse<List<ClienteResponse>> listar() {
        return ApiResponse.success(200, "Clientes consultados", clienteService.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<ClienteResponse> obtener(@PathVariable Long id) {
        return ApiResponse.success(200, "Cliente consultado", clienteService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> crear(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Cliente creado", clienteService.crear(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ClienteResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ApiResponse.success(200, "Cliente actualizado", clienteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
