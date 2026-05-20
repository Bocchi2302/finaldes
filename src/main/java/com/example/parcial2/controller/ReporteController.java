package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.DashboardResponse;
import com.papeleria.inteligente.dto.response.ProductoMasVendidoResponse;
import com.papeleria.inteligente.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {
    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard() {
        return ApiResponse.success(200, "Dashboard consultado", reporteService.dashboard());
    }

    @GetMapping("/productos-mas-vendidos")
    public ApiResponse<List<ProductoMasVendidoResponse>> productosMasVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "10") int limite) {
        return ApiResponse.success(200, "Productos más vendidos", reporteService.productosMasVendidos(desde, hasta, limite));
    }
}
