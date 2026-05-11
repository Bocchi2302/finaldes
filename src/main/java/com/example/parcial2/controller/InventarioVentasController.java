package com.example.parcial2.controller;

import com.example.parcial2.config.ApiResponseBuilder;
import com.example.parcial2.dto.request.ProductoRequest;
import com.example.parcial2.dto.request.VentaRequest;
import com.example.parcial2.dto.response.ApiResponse;
import com.example.parcial2.dto.response.ProductoResponse;
import com.example.parcial2.dto.response.VentaResponse;
import com.example.parcial2.service.ProductoCommandService;
import com.example.parcial2.service.ProductoQueryService;
import com.example.parcial2.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class InventarioVentasController {

    private final ProductoCommandService productoCommandService;
    private final ProductoQueryService productoQueryService;
    private final VentaService ventaService;
    private final ApiResponseBuilder responseBuilder;

    public InventarioVentasController(ProductoCommandService productoCommandService,
                                      ProductoQueryService productoQueryService,
                                      VentaService ventaService,
                                      ApiResponseBuilder responseBuilder) {
        this.productoCommandService = productoCommandService;
        this.productoQueryService = productoQueryService;
        this.ventaService = ventaService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/productos")
    public ResponseEntity<ApiResponse<ProductoResponse>> create(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse data = productoCommandService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.success(HttpStatus.CREATED, "Producto creado correctamente", data));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> getById(@PathVariable Long id) {
        ProductoResponse data = productoQueryService.obtenerProductoPorId(id);
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Producto obtenido correctamente", data));
    }

    @GetMapping("/productos")
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> getAll() {
        List<ProductoResponse> data = productoQueryService.listarProductos();
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Productos obtenidos correctamente", data));
    }

    @GetMapping("/inventario")
    public ResponseEntity<ApiResponse<List<ProductoResponse>>> inventory() {
        List<ProductoResponse> data = productoQueryService.listarProductos();
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Inventario obtenido correctamente", data));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> update(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        ProductoResponse data = productoCommandService.actualizarProducto(id, request);
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Producto actualizado correctamente", data));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productoCommandService.eliminarProducto(id);
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Producto desactivado correctamente", null));
    }

    @PostMapping("/ventas")
    public ResponseEntity<ApiResponse<VentaResponse>> addVenta(@Valid @RequestBody VentaRequest request) {
        VentaResponse data = ventaService.registrarVenta(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.success(HttpStatus.CREATED, "Venta registrada correctamente", data));
    }

    @GetMapping("/ventas")
    public ResponseEntity<ApiResponse<List<VentaResponse>>> listVentas() {
        List<VentaResponse> data = ventaService.listarVentas();
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Ventas obtenidas correctamente", data));
    }
}