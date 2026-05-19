package com.papeleria.inteligente.controller;

import com.papeleria.inteligente.dto.response.ApiResponse;
import com.papeleria.inteligente.dto.response.PrediccionResponse;
import com.papeleria.inteligente.service.PrediccionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/predicciones")
public class PrediccionController {
    private final PrediccionService prediccionService;

    public PrediccionController(PrediccionService prediccionService) {
        this.prediccionService = prediccionService;
    }

    @GetMapping("/{productoId}")
    public ApiResponse<PrediccionResponse> predecir(@PathVariable Long productoId,
                                                    @RequestParam(defaultValue = "7") int dias) {
        return ApiResponse.success(200, "Predicción calculada", prediccionService.predecir(productoId, dias));
    }
}
