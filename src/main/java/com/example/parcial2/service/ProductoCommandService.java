package com.example.parcial2.service;

import com.example.parcial2.dto.request.ProductoRequest;
import com.example.parcial2.dto.response.ProductoResponse;

public interface ProductoCommandService {
    ProductoResponse crearProducto(ProductoRequest request);
    ProductoResponse actualizarProducto(Long id, ProductoRequest request);
    void eliminarProducto(Long id);
}