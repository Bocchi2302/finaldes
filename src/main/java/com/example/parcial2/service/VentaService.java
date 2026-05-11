package com.example.parcial2.service;

import com.example.parcial2.dto.request.VentaRequest;
import com.example.parcial2.dto.response.VentaResponse;

import java.util.List;

public interface VentaService {
    VentaResponse registrarVenta(VentaRequest request);
    List<VentaResponse> listarVentas();
}