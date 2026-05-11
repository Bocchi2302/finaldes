package com.example.parcial2.service;

import com.example.parcial2.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoQueryService {
    ProductoResponse obtenerProductoPorId(Long id);
    List<ProductoResponse> listarProductos();
}