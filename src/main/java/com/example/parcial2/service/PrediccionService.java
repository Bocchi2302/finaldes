package com.example.parcial2.service;

import com.example.parcial2.dto.response.HistorialPrediccionResponse;
import com.example.parcial2.dto.response.PrediccionResponse;
import com.example.parcial2.entity.DetalleVenta;
import com.example.parcial2.entity.Producto;
import com.example.parcial2.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PrediccionService {
    private final ProductoService productoService;
    private final DetalleVentaRepository detalleVentaRepository;

    public PrediccionService(ProductoService productoService, DetalleVentaRepository detalleVentaRepository) {
        this.productoService = productoService;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public PrediccionResponse predecir(Long productoId, int diasProyectados) {
        Producto producto = productoService.obtenerEntidad(productoId);
        List<HistorialPrediccionResponse> historial = agruparHistorial(productoId);

        if (historial.size() < 2) {
            return new PrediccionResponse(
                    producto.getId(), producto.getNombre(), producto.getStock(), diasProyectados,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0,
                    "Se requieren al menos dos periodos de venta para calcular una predicción confiable.",
                    historial
            );
        }

        int n = historial.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        for (int i = 0; i < n; i++) {
            double x = i + 1;
            double y = historial.get(i).cantidadVendida();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double denominator = (n * sumX2) - (sumX * sumX);
        double pendiente = denominator == 0 ? 0 : ((n * sumXY) - (sumX * sumY)) / denominator;
        double intercepto = (sumY - pendiente * sumX) / n;

        double demanda = 0;
        for (int i = 1; i <= diasProyectados; i++) {
            demanda += Math.max(0, pendiente * (n + i) + intercepto);
        }
        int demandaRedondeada = (int) Math.ceil(demanda);
        int recomendacion = Math.max(0, demandaRedondeada - producto.getStock());
        String mensaje = recomendacion > 0
                ? "Se recomienda comprar al menos " + recomendacion + " unidades."
                : "El stock actual cubre la demanda estimada.";

        return new PrediccionResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                diasProyectados,
                BigDecimal.valueOf(pendiente).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(intercepto).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(demanda).setScale(2, RoundingMode.HALF_UP),
                recomendacion,
                mensaje,
                historial
        );
    }

    private List<HistorialPrediccionResponse> agruparHistorial(Long productoId) {
        Map<LocalDate, Integer> acumulado = new LinkedHashMap<>();
        for (DetalleVenta detalle : detalleVentaRepository.findHistorialProducto(productoId)) {
            acumulado.merge(detalle.getVenta().getFecha(), detalle.getCantidad(), Integer::sum);
        }
        return acumulado.entrySet().stream()
                .map(entry -> new HistorialPrediccionResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
}
